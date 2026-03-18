package org.tus.tx.service.message.mq.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

@Configuration
@EnableConfigurationProperties({MqProperties.class, RocketMqProperties.class, JmsProperties.class})
public class MqConfiguration {

    @Bean
    @ConditionalOnProperty(name = "mq.publisher", havingValue = "jms")
    public ConnectionFactory jmsConnectionFactory(JmsProperties props) {
        // Spring Boot 3 uses jakarta.jms; Artemis provides a jakarta-compatible ConnectionFactory.
        return new ActiveMQJMSConnectionFactory(props.getBrokerUrl(), props.getUsername(), props.getPassword());
    }

    @Bean
    @ConditionalOnProperty(name = "mq.publisher", havingValue = "jms")
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, JmsProperties props) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setPubSubDomain(props.isPubSubDomain());
        return template;
    }
}

