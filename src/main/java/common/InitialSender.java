package common;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@PropertySource("classpath:/../../resources/application.properties")
public class InitialSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Environment env;

    public void runInitial() throws Exception {
        // send initial message to queue for parsing
        String initialMessage = env.getProperty("rabbitmq.init_message");
        rabbitTemplate.send("for_parsing", new Message(initialMessage.getBytes(), null));
    }

}
