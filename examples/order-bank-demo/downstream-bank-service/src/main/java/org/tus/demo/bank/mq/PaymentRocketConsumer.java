package org.tus.demo.bank.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.tus.demo.bank.service.BankIngestService;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "bank.mq", name = "enabled", havingValue = "true")
public class PaymentRocketConsumer implements InitializingBean, DisposableBean {
    private final BankIngestService ingestService;


    @Value("${bank.mq.name-server}")
    private String nameServer;

    @Value("${bank.mq.topic}")
    private String topic;

    @Value("${bank.mq.consumer-group}")
    private String consumerGroup;

    private DefaultMQPushConsumer consumer;

    @Override
    public void afterPropertiesSet() throws Exception {
        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        consumer.subscribe(topic, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt m : msgs) {
                try {
                    String messageId = m.getKeys();
                    if (messageId == null || messageId.isBlank()) {
                        messageId = m.getMsgId();
                    }
                    String traceId = m.getUserProperty("TRACE_ID");
                    String body = new String(m.getBody(), StandardCharsets.UTF_8);
                    ingestService.ingest(messageId, traceId, body);
                } catch (Exception e) {
                    log.error("Bank MQ consume field msgId={}", m.getMsgId(), e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("RocketMQ push consumer started topic={} group={} namesrv={}", topic,
                consumerGroup, nameServer);
    }

    @Override
    public void destroy() throws Exception {
        if (consumer != null) {
            consumer.shutdown();
            log.info("RocketMQ consumer stopped");
        }
    }
}
