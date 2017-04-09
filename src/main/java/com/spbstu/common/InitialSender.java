package com.spbstu.common;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("INITIAL")
@PropertySource("classpath:application.properties")
public class InitialSender implements CommandLineRunner {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Environment env;
    @Autowired
    private ConfigurableApplicationContext context;

    private static String CRAWLER_QUEUE = "for_parsing";

    @Override
    public void run(String... args) throws Exception {
        // send initial message to queue for parsing
        String initialMessage = env.getProperty("rabbitmq.init_message");
        rabbitTemplate.send(CRAWLER_QUEUE, new Message(initialMessage.getBytes(), new MessageProperties()));
        context.close();
    }

}
