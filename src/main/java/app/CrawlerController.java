package java.app;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
@EnableRabbit
public class CrawlerController {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Environment env;

    private static final int PARSE_LIMIT = 100000;
    private static int counter = 0;

    @RabbitListener(queues = "for_parsing")
    public void runCrawler(String message) throws Exception {
        if (counter < PARSE_LIMIT) {
            List<String> links = new ArrayList<String>();
            Parser.parsePage(message, links);

            putLinksForParsingToQueue(links);
            putLinksForDownloadingToQueue(message);

            // TODO: add link to mongo visited pages

            counter++;
        }
    }

    private void putLinksForParsingToQueue(List<String> links) {
        int linksSize = links.size();
        for (int i = 0; i < linksSize; i++) {
            String message = links.get(i);
            // TODO: check property in MongoDB
            rabbitTemplate.convertAndSend(
                    env.getProperty("for_parsing"), message);
        }
    }

    private void putLinksForDownloadingToQueue(String... files) {
        int filesSize = files.length;
        for (int i = 0; i < filesSize; i++) {
            String message = files[i];
            rabbitTemplate.convertAndSend(
                    env.getProperty("for_downloading"), message);
        }
    }
}
