package org.tus.tx.rms.mq;

/**
 * Publishes committed outbox / two-phase messages to downstream (RocketMQ, etc.).
 * Default implementation logs only — replace with real producer in production.
 */
public interface DownstreamPublisher {

    void publish(String destination, String payload, String messageId, String traceId);
}
