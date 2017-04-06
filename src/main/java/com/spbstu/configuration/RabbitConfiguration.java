package com.spbstu.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class RabbitConfiguration {

    @Autowired
    Environment env;
    @Autowired
    MessageListener customListener;
    @Autowired
    Queue customQueue;

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory());
        container.setQueueNames(customQueue.getName());
        container.setMessageListener(customListener);
        return container;
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(env.getProperty("rabbitmq.host"));
        connectionFactory.setUsername(env.getProperty("rabbitmq.admin"));
        connectionFactory.setPassword(env.getProperty("rabbitmq.password"));
        connectionFactory.setPort(Integer.valueOf(env.getProperty("rabbitmq.port")));
        connectionFactory.setVirtualHost(env.getProperty("rabbitmq.vhost"));
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory());
        rabbitAdmin.declareQueue(crawlerQueue());
        rabbitAdmin.declareQueue(downloaderQueue());
        return rabbitAdmin;
    }

    @Bean
    public AmqpTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public Queue crawlerQueue() {
        return new Queue("for_parsing");
    }

    @Bean
    public Queue downloaderQueue() {
        return new Queue("for_downloading");
    }
}
