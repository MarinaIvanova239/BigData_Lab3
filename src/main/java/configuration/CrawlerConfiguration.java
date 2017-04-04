package configuration;

import listeners.CrawlerMessageListener;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfiguration {

    @Bean(name = "customListener")
    public MessageListener customListener() {
        return new CrawlerMessageListener();
    }

    @Bean(name = "customQueue")
    public Queue customQueue() {
        return new Queue("for_parsing");
    }
}
