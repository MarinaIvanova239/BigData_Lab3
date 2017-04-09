package com.spbstu.listeners;

import com.spbstu.common.Parser;
import com.spbstu.database.MongoDbService;
import com.spbstu.database.documents.VisitedPages;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class CrawlerMessageListener implements MessageListener {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private MongoDbService database;
    @Autowired
    private ConfigurableApplicationContext context;

    private static final int PARSE_LIMIT = 100000;
    private static int counter = 0;
    private static String CRAWLER_QUEUE = "for_parsing";
    private static String DOWNLOADER_QUEUE = "for_downloading";

    @Override
    public void onMessage(Message message) {
        try {
            String link = new String(message.getBody(), "UTF-8");

            List<String> linksOnPage = new ArrayList<String>();
            Parser.parsePage(link, linksOnPage);

            // save link to visited pages table
            database.contains(new VisitedPages(link));

            // save all links in queue
            putLinksForParsingToQueue(linksOnPage);
            putLinksForDownloadingToQueue(link);
            counter++;

            if (counter > PARSE_LIMIT) {
                context.close();
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
                rabbitTemplate.send(CRAWLER_QUEUE, new Message(message.getBytes(), new MessageProperties()));
            }
        }
    }

    private void putLinksForDownloadingToQueue(String link) {
        // put page in downloading queue
        rabbitTemplate.send(DOWNLOADER_QUEUE, new Message(link.getBytes(), new MessageProperties()));
    }
}
