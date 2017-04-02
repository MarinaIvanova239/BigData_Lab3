package java.app;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import java.db.entities.VisitedPages;
import java.util.ArrayList;
import java.util.List;

@Controller
@EnableRabbit
public class CrawlerController {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Environment env;
    private MongoDatabase database;

    private static final int PARSE_LIMIT = 100000;
    private static int counter = 0;

    @RabbitListener(queues = "for_parsing")
    public void runCrawler(String message) throws Exception {
        if (counter < PARSE_LIMIT) {
            List<String> links = new ArrayList<String>();
            Parser.parsePage(message, links);

            // save link to visited pages table
            database.contains(new VisitedPages(message));

            // save all links in queue
            putLinksForParsingToQueue(links);
            putLinksForDownloadingToQueue(message);

            counter++;
        }
    }

    private void putLinksForParsingToQueue(List<String> links) {
        int linksSize = links.size();
        for (int i = 0; i < linksSize; i++) {
            String message = links.get(i);
            // if page wasn't visited, put it to queue
            if (!database.containLink(message)) {
                rabbitTemplate.convertAndSend("for_parsing", message);
            }
        }
    }

    private void putLinksForDownloadingToQueue(String file) {
        // put page in
        rabbitTemplate.convertAndSend("for_downloading", file);
    }
}
