package org.tus.tx.service.message.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * No-op implementation of ReliableMessagePublisher for compilation and testing.
 * Replace with a real implementation (e.g. RocketMQ) in the app and register it as the bean.
 */
@Component
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(name = "mq.publisher", havingValue = "stub")
public class StubReliableMessagePublisher implements ReliableMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(StubReliableMessagePublisher.class);

    public StubReliableMessagePublisher() {
    }

    @Override
    public void send(String consumerQueue, String messageBody) {
        log.debug("StubReliableMessagePublisher.send queue={} bodyLength={}", consumerQueue,
                messageBody != null ? messageBody.length() : 0);
    }
}
