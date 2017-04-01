package java.app;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

@Controller
@PropertySource("classpath:/../../resources/application.properties")
public class InitialController {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Environment env;

    public void initRabbit() throws Exception {
        rabbitTemplate.convertAndSend("for_parsing",
                env.getProperty("rabbitmq.init_message"));
    }

}
