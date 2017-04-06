package com.spbstu.configuration;

import com.spbstu.listeners.DownloaderMessageListener;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("DOWNLOADER")
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
