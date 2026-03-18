package org.tus.tx.service.message.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mq")
public class MqProperties {

    /**
     * rocketmq | jms
     */
    private String publisher = "rocketmq";

    private final RocketMqProperties rocketmq = new RocketMqProperties();
    private final JmsProperties jms = new JmsProperties();

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public RocketMqProperties getRocketmq() {
        return rocketmq;
    }

    public JmsProperties getJms() {
        return jms;
    }
}

