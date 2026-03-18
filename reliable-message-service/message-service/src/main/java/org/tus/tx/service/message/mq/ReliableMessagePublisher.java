package org.tus.tx.service.message.mq;

/**
 * Internal publisher abstraction for sending messages to MQ.
 * <p>
 * Kept inside the implementation module so the API module does not expose MQ concerns.
 */
public interface ReliableMessagePublisher {
    void send(String consumerQueue, String messageBody);
}

