package org.tus.tx.rms.service.twophase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tus.tx.rms.domain.twophase.RmsMessageResultEntity;
import org.tus.tx.rms.domain.twophase.RmsMessageResultRepository;
import org.tus.tx.rms.domain.twophase.RmsMessageTxEntity;
import org.tus.tx.rms.domain.twophase.RmsMessageTxRepository;
import org.tus.tx.rms.mq.DownstreamPublisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwoPhaseReliableMessageService {

    public static final String PREPARED = "PREPARED";
    public static final String COMMITTED = "COMMITTED";
    public static final String ROLLED_BACK = "ROLLED_BACK";
    public static final String SENDING = "SENDING";
    public static final String SENT = "SENT";
    public static final String DEAD = "DEAD";

    private final RmsMessageTxRepository txRepository;
    private final RmsMessageResultRepository resultRepository;
    private final DownstreamPublisher downstreamPublisher;

    @Transactional
    public void prepare(String messageId, String bizKey, String eventType, String destination,
                        String payload, String traceId, int ttlSeconds) {
        Optional<RmsMessageTxEntity> existing = txRepository.findByMessageId(messageId);
        if (existing.isPresent()) {
            RmsMessageTxEntity e = existing.get();
            if (samePrepare(e, bizKey, eventType, destination, payload)) {
                return;
            }
            throw new IllegalArgumentException("message_id already exists with different payload: " + messageId);
        }
        RmsMessageTxEntity row = new RmsMessageTxEntity();
        row.setMessageId(messageId);
        row.setBizKey(bizKey);
        row.setEventType(eventType);
        row.setDestination(destination);
        row.setPayload(payload);
        row.setTraceId(traceId);
        row.setTtlSeconds(ttlSeconds > 0 ? ttlSeconds : null);
        row.setStatus(PREPARED);
        row.setSendTimes(0);
        txRepository.save(row);
    }

    private boolean samePrepare(RmsMessageTxEntity e, String bizKey, String eventType, String destination, String payload) {
        return java.util.Objects.equals(e.getBizKey(), bizKey)
                && java.util.Objects.equals(e.getEventType(), eventType)
                && java.util.Objects.equals(e.getDestination(), destination)
                && java.util.Objects.equals(e.getPayload(), payload);
    }

    @Transactional
    public void commit(String messageId, String bizKey) {
        RmsMessageTxEntity row = load(messageId);
        if (!bizKey.equals(row.getBizKey())) {
            throw new IllegalArgumentException("biz_key mismatch");
        }
        if (!PREPARED.equals(row.getStatus())) {
            throw new IllegalStateException("invalid status for commit: " + row.getStatus());
        }
        row.setStatus(COMMITTED);
        row.setNextRetryAt(null);
        row.setLastError(null);
        txRepository.save(row);
    }

    @Transactional
    public void rollback(String messageId, String reason) {
        RmsMessageTxEntity row = load(messageId);
        if (!PREPARED.equals(row.getStatus())) {
            throw new IllegalStateException("invalid status for rollback: " + row.getStatus());
        }
        row.setStatus(ROLLED_BACK);
        row.setLastError(reason);
        txRepository.save(row);
    }

    @Transactional(readOnly = true)
    public RmsMessageTxEntity get(String messageId) {
        return load(messageId);
    }

    @Transactional
    public void reportConsumeResult(String messageId, String bizKey, String resultCode,
                                    String resultPayload, String errorCode, String errorMessage) {
        RmsMessageTxEntity row = load(messageId);
        if (!bizKey.equals(row.getBizKey())) {
            throw new IllegalArgumentException("biz_key mismatch");
        }
        RmsMessageResultEntity r = new RmsMessageResultEntity();
        r.setMessageId(messageId);
        r.setBizKey(bizKey);
        r.setResultCode(resultCode);
        r.setResultPayload(resultPayload);
        r.setErrorCode(errorCode);
        r.setErrorMessage(errorMessage);
        resultRepository.save(r);
    }

    @Transactional
    public void relayTick(int batchSize, int retryIntervalSeconds) {
        int limit = batchSize <= 0 ? 50 : batchSize;
        Instant now = Instant.now();
        List<RmsMessageTxEntity> due = txRepository.findDueForRelay(COMMITTED, now, PageRequest.of(0, limit));
        for (RmsMessageTxEntity row : due) {
            row.setStatus(SENDING);
            txRepository.save(row);
            try {
                downstreamPublisher.publish(row.getDestination(), row.getPayload(), row.getMessageId(), row.getTraceId());
                row.setStatus(SENT);
                row.setLastError(null);
                row.setNextRetryAt(null);
            } catch (Exception ex) {
                int next = row.getSendTimes() + 1;
                row.setSendTimes(next);
                row.setLastError(trim(ex.getMessage(), 1800));
                if (next >= row.getMaxRetryTimes()) {
                    row.setStatus(DEAD);
                    row.setNextRetryAt(null);
                } else {
                    row.setStatus(COMMITTED);
                    int sec = retryIntervalSeconds <= 0 ? 30 : retryIntervalSeconds;
                    row.setNextRetryAt(now.plus(sec, ChronoUnit.SECONDS));
                }
            }
            txRepository.save(row);
        }
    }

    private RmsMessageTxEntity load(String messageId) {
        return txRepository.findByMessageId(messageId)
                .orElseThrow(() -> new IllegalArgumentException("message not found: " + messageId));
    }

    private static String trim(String s, int max) {
        if (s == null) {
            return null;
        }
        return s.length() <= max ? s : s.substring(0, max);
    }
}
