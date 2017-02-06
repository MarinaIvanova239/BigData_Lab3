package app.src;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.*;

public class MainClass {

    public final static String PARSING_QUEUE_NAME = "for_parsing";
    public final static String DOWNLOADING_QUEUE_NAME = "for_downloading";

    private static int crawlersNumber = 0;
    private static int downloadersNumber = 0;
    private static String rabbimqHost = "localhost";
    private static String initMessage = null;

    private static int readConfig(String fileName) throws Exception {
        FileReader input = new FileReader(fileName);
        BufferedReader bufRead = new BufferedReader(input);
        String line;

        // read config settings
        while ( (line = bufRead.readLine()) != null)
        {
            String[] array = line.split(":");
            if (array[0].equals("crawlers")) {
                crawlersNumber = Integer.parseInt(array[1]);
            } else if (array[0].equals("downloaders")) {
                downloadersNumber = Integer.parseInt(array[1]);
            } else if (array[0].equals("rabbitmqHost")) {
                rabbimqHost = array[1];
            } else if (array[0].equals("initMessage")) {
                initMessage = array[1];
            }else {
                return 1;
            }
        }

        return 0;
    }

    private static void initRabbitMQ() throws Exception {
        // initiliaze connection with rabbimq server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbimqHost);
        Connection connection = factory.newConnection();
        Channel rabbitmqChannel = connection.createChannel();

        // create queue for parsing and put wikipedia link there
        rabbitmqChannel.queueDeclare(PARSING_QUEUE_NAME, true, false, false, null);
        rabbitmqChannel.basicPublish("", PARSING_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN, initMessage.getBytes());

        // create queue for downloading
        rabbitmqChannel.queueDeclare(DOWNLOADING_QUEUE_NAME, true, false, false, null);

        // close connection
        rabbitmqChannel.close();
        connection.close();
    }

    private static void runCrawlers() throws Exception {
        // init new controller
        VisitedPagesController controller = new VisitedPagesController();
        // run crawler's instances
        for (int i = 0; i < crawlersNumber; i++) {
            Crawler crawler = new Crawler(rabbimqHost, controller);
            crawler.run();
        }
    }

    private static void runDownloaders() throws Exception {
        // run downloader's instances
        for (int i = 0; i < downloadersNumber; i++) {
            Downloader downloader = new Downloader(rabbimqHost);
            downloader.run();
        }
    }

    public static void main(String[] args) throws Exception {
        int resultCode = readConfig(args[0]);
        if (resultCode == 1)
            System.exit(1);

        initRabbitMQ();

        runCrawlers();
        runDownloaders();
    }
}
