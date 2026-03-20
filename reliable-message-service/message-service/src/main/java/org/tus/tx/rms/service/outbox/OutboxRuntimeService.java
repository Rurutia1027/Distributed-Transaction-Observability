package org.tus.tx.rms.service.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tus.tx.rms.domain.outbox.RmsOutboxEventEntity;
import org.tus.tx.rms.domain.outbox.RmsOutboxEventRepository;
import org.tus.tx.rms.mq.DownstreamPublisher;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Demo outbox runtime backed by {@code rms_outbox_event} in RMS DB.
 * In production, outbox usually lives in the business service DB.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rms.outbox.enabled", havingValue = "true", matchIfMissing = true)
public class OutboxRuntimeService {

    public static final String PENDING = "PENDING";
    public static final String SENDING = "SENDING";
    public static final String SENT = "SENT";
    public static final String RETRYING = "RETRYING";
    public static final String DEAD = "DEAD";

    private final RmsOutboxEventRepository repository;
    private final DownstreamPublisher downstreamPublisher;

    @Transactional
    public void append(String eventId, String aggregateType, String aggregateId, String bizKey,
                       String eventType, String destination, String payload, String headersJson, String traceId) {
        Optional<RmsOutboxEventEntity> existing = repository.findByEventId(eventId);
        if (existing.isPresent()) {
            RmsOutboxEventEntity e = existing.get();
            if (sameAppend(e, aggregateType, aggregateId, bizKey, eventType, destination, payload)) {
                return;
            }
            throw new IllegalArgumentException("event_id already exists with different data: " + eventId);
        }
        RmsOutboxEventEntity row = new RmsOutboxEventEntity();
        row.setEventId(eventId);
        row.setAggregateType(aggregateType);
        row.setAggregateId(aggregateId);
        row.setBizKey(bizKey);
        row.setEventType(eventType);
        row.setDestination(destination);
        row.setPayload(payload);
        row.setHeadersJson(headersJson);
        row.setTraceId(traceId);
        row.setStatus(PENDING);
        row.setReadyToSend(false);
        row.setSendTimes(0);
        repository.save(row);
    }

    private boolean sameAppend(RmsOutboxEventEntity e, String aggregateType, String aggregateId,
                               String bizKey, String eventType, String destination, String payload) {
        return java.util.Objects.equals(e.getAggregateType(), aggregateType)
                && java.util.Objects.equals(e.getAggregateId(), aggregateId)
                && java.util.Objects.equals(e.getBizKey(), bizKey)
                && java.util.Objects.equals(e.getEventType(), eventType)
                && java.util.Objects.equals(e.getDestination(), destination)
                && java.util.Objects.equals(e.getPayload(), payload);
    }

    @Transactional
    public void markReady(String eventId) {
        RmsOutboxEventEntity row = load(eventId);
        row.setReadyToSend(true);
        row.setNextRetryAt(Instant.now());
        repository.save(row);
    }

    @Transactional(readOnly = true)
    public RmsOutboxEventEntity query(String eventId) {
        return load(eventId);
    }

    @Transactional
    public void relayTick(int batchSize, int retryIntervalSeconds) {
        int limit = batchSize <= 0 ? 50 : batchSize;
        Instant now = Instant.now();
        List<RmsOutboxEventEntity> due = repository.findDueForRelay(PENDING, now, PageRequest.of(0, limit));
        for (RmsOutboxEventEntity row : due) {
            row.setStatus(SENDING);
            repository.save(row);
            try {
                downstreamPublisher.publish(row.getDestination(), row.getPayload(), row.getEventId(), row.getTraceId());
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
                    row.setStatus(RETRYING);
                    int sec = retryIntervalSeconds <= 0 ? 30 : retryIntervalSeconds;
                    row.setNextRetryAt(now.plus(sec, ChronoUnit.SECONDS));
                }
            }
            repository.save(row);
        }
        // also pick RETRYING that are due
        List<RmsOutboxEventEntity> retryDue = repository.findDueForRelay(RETRYING, now, PageRequest.of(0, limit));
        for (RmsOutboxEventEntity row : retryDue) {
            row.setStatus(SENDING);
            repository.save(row);
            try {
                downstreamPublisher.publish(row.getDestination(), row.getPayload(), row.getEventId(), row.getTraceId());
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
                    row.setStatus(RETRYING);
                    int sec = retryIntervalSeconds <= 0 ? 30 : retryIntervalSeconds;
                    row.setNextRetryAt(now.plus(sec, ChronoUnit.SECONDS));
                }
            }
            repository.save(row);
        }
    }

    private RmsOutboxEventEntity load(String eventId) {
        return repository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("event not found: " + eventId));
    }

    private static String trim(String s, int max) {
        if (s == null) {
            return null;
        }
        return s.length() <= max ? s : s.substring(0, max);
    }
}
