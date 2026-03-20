package org.tus.tx.rms.service;


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

import static org.tus.tx.rms.domain.enums.MessageStatus.COMMITTED;
import static org.tus.tx.rms.domain.enums.MessageStatus.DEAD;
import static org.tus.tx.rms.domain.enums.MessageStatus.PREPARED;
import static org.tus.tx.rms.domain.enums.MessageStatus.ROLLED_BACK;
import static org.tus.tx.rms.domain.enums.MessageStatus.SENDING;

@Service
@RequiredArgsConstructor
public class TwoPhaseReliableMessageService {
    private final RmsMessageTxRepository txRepository;
    private final RmsMessageResultRepository resultRepository;
    private final DownstreamPublisher downStreamPublisher;

    @Transactional
    public void prepare(String messageId, String bizKey, String eventType, String destination,
                        String payload, String traceId, int ttlSeconds) {
        Optional<RmsMessageTxEntity> existing = txRepository.findByMessageId(messageId);
        if (existing.isPresent()) {
            RmsMessageTxEntity e = existing.get();
            if (samePrepare(e, bizKey, eventType, destination, payload)) {
                return;
            }
            throw new IllegalArgumentException("message_id already exists with different " +
                    "payload: " + messageId);
        }

        RmsMessageTxEntity row = new RmsMessageTxEntity();
        row.setMessageId(messageId);
        row.setBizKey(bizKey);
        row.setEventType(eventType);
        row.setDestination(destination);
        row.setPayload(payload);
        row.setTraceId(traceId);
        row.setTtlSeconds(ttlSeconds > 0 ? ttlSeconds : null);
        row.setStatus(PREPARED.getValue());
        row.setSendTimes(0);

        txRepository.save(row);
    }

    private boolean samePrepare(RmsMessageTxEntity e, String bizKey, String eventType,
                                String destination, String payload) {
        return java.util.Objects.equals(e.getBizKey(), bizKey)
                && java.util.Objects.equals(e.getEventType(), eventType)
                && java.util.Objects.equals(e.getDestination(), destination)
                && java.util.Objects.equals(e.getPayload(), payload);
    }

    @Transactional
    public void commit(String messageId, String bizKey) {
        RmsMessageTxEntity row = load(messageId);
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


    @Transactional
    public void rollback(String messageId, String reason) {
        RmsMessageTxEntity row = load(messageId);
        if (!PREPARED.equals(row.getStatus())) {
            throw new IllegalStateException("invalid status for rollback: " + row.getStatus());
        }
        row.setStatus(ROLLED_BACK.getValue());
        row.setLastError(reason);
        txRepository.save(row);
    }

    @Transactional
    public RmsMessageTxEntity get(String messageId) {
        return load(messageId);
    }

    @Transactional
    public void reportConsumeResult(String messageId, String bizKey, String resultCode,
                                    String resultPayload, String errorCode,
                                    String errorMessage) {
        RmsMessageTxEntity row = load(messageId);
        if (!bizKey.equals(row.getBizKey())) {
            throw new IllegalArgumentException("biz_key mismatch");
        }
        RmsMessageResultEntity r = new RmsMessageResultEntity();
        r.setMessageId(messageId);
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
        List<RmsMessageTxEntity> due = txRepository.findDueForRelay(COMMITTED.getValue(),
                now, PageRequest.of(0, limit));
        for (RmsMessageTxEntity row : due) {
            row.setStatus(SENDING.getValue());
            txRepository.save(row);
            try {
                downStreamPublisher.publish(row.getDestination(), row.getPayload(),
                        row.getMessageId(), row.getTraceId());
                row.setStatus(SENDING.getValue());
                row.setLastError(null);
                row.setNextRetryAt(now);
            } catch (Exception e) {
                int next = row.getSendTimes() + 1;
                row.setSendTimes(next);
                row.setLastError(trim(e.getMessage(), 1800));
                if (next >= row.getMaxRetryTimes()) {
                    row.setStatus(DEAD.getValue());
                    row.setNextRetryAt(null);
                } else {
                    row.setStatus(COMMITTED.getValue());
                    int sec = retryIntervalSeconds <= 0 ? 30 : retryIntervalSeconds;
                    row.setNextRetryAt(now.plus(sec, ChronoUnit.SECONDS));
                }
            }

            txRepository.save(row);
        }
    }
}
