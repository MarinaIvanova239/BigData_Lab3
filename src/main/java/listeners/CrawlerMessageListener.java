package listeners;

import common.Parser;
import database.MongoDbService;
import database.entities.VisitedPages;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

public class CrawlerMessageListener implements MessageListener {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Environment env;
    @Autowired
    private MongoDbService database;

    private static final int PARSE_LIMIT = 100000;
    private static int counter = 0;

    @Override
    public void onMessage(Message message) {
        try {
            String link = new String(message.getBody(), "UTF-8");
            if (counter < PARSE_LIMIT) {
                List<String> linksOnPage = new ArrayList<String>();
                Parser.parsePage(link, linksOnPage);

                // save link to visited pages table
                database.contains(new VisitedPages(link));

                // save all links in queue
                putLinksForParsingToQueue(linksOnPage);
                putLinksForDownloadingToQueue(link);

                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putLinksForParsingToQueue(List<String> links) {
        int linksSize = links.size();
        for (int i = 0; i < linksSize; i++) {
            String message = links.get(i);
            // if page wasn't visited, put it to queue
            if (!database.containLink(message)) {
                rabbitTemplate.send("for_parsing", new Message(message.getBytes(), null));
            }
        }
    }

    private void putLinksForDownloadingToQueue(String file) {
        // put page in downloading queue
        rabbitTemplate.send("for_downloading", new Message(file.getBytes(), null));
    }
}
