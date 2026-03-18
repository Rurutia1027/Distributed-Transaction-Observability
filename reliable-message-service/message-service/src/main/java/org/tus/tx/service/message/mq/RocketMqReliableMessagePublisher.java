package org.tus.tx.service.message.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.tus.tx.service.message.mq.config.RocketMqProperties;

/**
 * RocketMQ publisher skeleton.
 * <p>
 * This wires RocketMQ config from application.yml. Replace the send() body with the real
 * RocketMQ client implementation when integrating the RocketMQ SDK.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mq.publisher", havingValue = "rocketmq", matchIfMissing = true)
public class RocketMqReliableMessagePublisher implements ReliableMessagePublisher {

    private final RocketMqProperties rocketMqProperties;

    @Override
    public void send(String consumerQueue, String messageBody) {
        // TODO: integrate RocketMQ producer (nameServer, producerGroup, timeout, retry)
        log.info("RocketMQ send placeholder. nameServer={}, group={}, queue={}, bodyLength={}",
                rocketMqProperties.getNameServer(),
                rocketMqProperties.getProducerGroup(),
                consumerQueue,
                messageBody != null ? messageBody.length() : 0);
    }
}

