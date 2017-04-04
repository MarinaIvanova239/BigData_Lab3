package configuration;

import listeners.DownloaderMessageListener;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloaderConfiguration {

    @Bean(name = "customListener")
    public MessageListener customListener() {
        return new DownloaderMessageListener();
    }

    @Bean(name = "customQueue")
    public Queue customQueue() {
        return new Queue("for_downloading");
    }
}
