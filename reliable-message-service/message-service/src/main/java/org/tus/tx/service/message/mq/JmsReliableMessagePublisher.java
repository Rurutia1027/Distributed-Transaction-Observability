package org.tus.tx.service.message.mq;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mq.publisher", havingValue = "jms")
public class JmsReliableMessagePublisher implements ReliableMessagePublisher {

    private final JmsTemplate jmsTemplate;

    @Override
    public void send(String consumerQueue, String messageBody) {
        jmsTemplate.convertAndSend(consumerQueue, messageBody);
    }
}

