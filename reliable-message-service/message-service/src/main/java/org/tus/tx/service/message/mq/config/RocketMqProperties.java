package org.tus.tx.service.message.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mq.rocketmq")
public class RocketMqProperties {
    private String nameServer;
    private String producerGroup;
    private Integer sendTimeoutMs = 3000;
    private Integer retryTimes = 2;

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public Integer getSendTimeoutMs() {
        return sendTimeoutMs;
    }

    public void setSendTimeoutMs(Integer sendTimeoutMs) {
        this.sendTimeoutMs = sendTimeoutMs;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }
}

