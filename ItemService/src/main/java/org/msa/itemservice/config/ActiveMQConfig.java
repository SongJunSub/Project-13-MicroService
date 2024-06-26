package org.msa.itemservice.config;

import jakarta.jms.Queue;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class ActiveMQConfig {

    //@Value("${activemq.broker.url}")
    private String brokerURL = "tcp://localhost:61616";

    //@Value("${activemq.broker.topic}")
    private String topic = "item-history";

    @Bean
    public Queue activeMQQueue() {
        return new ActiveMQQueue(topic);
    }

    // ActiveMQ에 데이터를 넣고 빼기 위해 사용하는 객체이다.
    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(activeMQConnectionFactory());
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();

        activeMQConnectionFactory.setBrokerURL(brokerURL);

        return activeMQConnectionFactory;
    }

}