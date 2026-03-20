package org.tus.tx.rms.mq;

/**
 * Publishes committed outbox / two-phase messages to downstream (RocketMQ, Kafka, etc.).
 * Default implementation logs only - replace with real producer in production.
 */
public interface DownstreamPublisher {
    /**
     * Publish a message to downstream system.
     *
     * @param destination target topic/queue or logical destination
     * @param payload     message body
     * @param messageId   unique identifier for idempotency and tracking
     * @param traceId     tracing context identifier used for distributed tracing
     *                    <p>
     *                    The {@code traceId} is introduced to support further integration with observability
     *                    frameworks such as OpenTelemetry. It allows propagation of tracing context across
     *                    asynchronous message boundaries, ensuring that message production
     *                    and consumption can be correlated within the same distributed trace.
     *                    </p>
     *                    <p>
     *                    This is particularly important in event-driven or message-based
     *                    systems, where execution context does not automatically flow like
     *                    in synchronous HTTP calls. By explicitly passing {@code traceId},
     *                    downstream consumers can continue the trace and provide end-to-end
     *                    visibility.
     *                    </p>
     */
    void publish(String destination, String payload, String messageId, String traceId);
}
