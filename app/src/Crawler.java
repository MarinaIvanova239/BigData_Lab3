package app.src;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {

    private static final int PARSE_LIMIT = 100000;

    private String rabbitmqHost;
    private Channel rabbitmqChannel;
    private VisitedPagesController controller;

    Crawler(String rabbitmqHost, VisitedPagesController controller) {
        this.rabbitmqHost = rabbitmqHost;
        this.controller = controller;
    }

    public void run() throws Exception {

        // initiliaze connection with rabbimq server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        Connection connection = factory.newConnection();
        rabbitmqChannel = connection.createChannel();

        // check or create queue for parsing
        rabbitmqChannel.queueDeclare(MainClass.PARSING_QUEUE_NAME, true, false, false, null);

        // check or create queue for downloading
        rabbitmqChannel.queueDeclare(MainClass.DOWNLOADING_QUEUE_NAME, true, false, false, null);

        // set prefetchCount = 1
        rabbitmqChannel.basicQos(1);

        // set personal handler to consumer
        final Consumer consumer = new DefaultConsumer(rabbitmqChannel) {
            private int depth = 0;

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    if (depth > PARSE_LIMIT) {
                        getChannel().close();
                    }

                    // get new links and files to download
                    List<String> links = new ArrayList<String>();
                    Parser.parsePage(message, links);

                    // put files and links to rabbimq queues
                    putLinksForParsingToQueue(links);
                    putLinksForDownloadingToQueue(message);

                    controller.addVisitedLink(message);
                    depth++;
                } catch (Exception e) {
                    System.out.println("Exception was handled: " + e.toString());
                } finally {
                    rabbitmqChannel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // set consumer listen to queue
        rabbitmqChannel.basicConsume(MainClass.PARSING_QUEUE_NAME, false, consumer);
    }

    private void putLinksForParsingToQueue(List<String> links) throws Exception {
        int linksSize = links.size();
        for (int i = 0; i < linksSize; i++) {
            String message = links.get(i);
            if (! controller.checkIfLinkWasVisited(message) ) {
                rabbitmqChannel.basicPublish("", MainClass.PARSING_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            }
        }
    }

    private void putLinksForDownloadingToQueue(String... files) throws Exception {
        int filesSize = files.length;
        for (int i = 0; i < filesSize; i++) {
            String message = files[i];
            rabbitmqChannel.basicPublish("", MainClass.DOWNLOADING_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        }
    }

}