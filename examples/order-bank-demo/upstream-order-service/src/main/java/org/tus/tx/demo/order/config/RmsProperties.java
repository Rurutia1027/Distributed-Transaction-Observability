package org.tus.tx.demo.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rms")
public class RmsProperties {

    private String host = "127.0.0.1";
    private int port = 9090;
    private final Client client = new Client();
    private final Payment payment = new Payment();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Client getClient() {
        return client;
    }

    public Payment getPayment() {
        return payment;
    }

    public static class Client {
        private int deadlineSeconds = 10;

        public int getDeadlineSeconds() {
            return deadlineSeconds;
        }

        public void setDeadlineSeconds(int deadlineSeconds) {
            this.deadlineSeconds = deadlineSeconds;
        }
    }

    public static class Payment {
        private String destination = "BANK_PAYMENT_COMMANDS";
        private int ttlSeconds = 86400;

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public int getTtlSeconds() {
            return ttlSeconds;
        }

        public void setTtlSeconds(int ttlSeconds) {
            this.ttlSeconds = ttlSeconds;
        }
    }
}
