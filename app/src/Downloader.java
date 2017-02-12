package app.src;

import com.rabbitmq.client.*;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Downloader {

    private static final int DOWNLOAD_LIMIT = 100000;

    private String rabbitmqHost;
    private Channel rabbitmqChannel;
    private VisitedPagesController controller;

    Downloader(String rabbitmqHost, VisitedPagesController controller) {
        this.rabbitmqHost = rabbitmqHost;
        this.controller = controller;
    }

    public void run() throws Exception {
        // initiliaze connection with rabbimq server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitmqHost);
        Connection connection = factory.newConnection();
        rabbitmqChannel = connection.createChannel();

        // check or create queue for downloading
        rabbitmqChannel.queueDeclare(MainClass.DOWNLOADING_QUEUE_NAME, true, false, false, null);

        // set prefetchCount = 1
        rabbitmqChannel.basicQos(1);

        // set personal handler to consumer
        final Consumer consumer = new DefaultConsumer(rabbitmqChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    // TODO: stop after DOWNLOAD_LIMIT steps
                    String dir = controller.getVisitedLinkDir(message);
                    downloadFile(message, dir);
                } catch (Exception e) {
                    System.out.println("Exception was handled: " + e.toString());
                } finally {
                    rabbitmqChannel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        // set consumer listen to queue
        rabbitmqChannel.basicConsume(MainClass.DOWNLOADING_QUEUE_NAME, false, consumer);
    }

    public void downloadFile(String link, String saveDir) throws Exception {

        // if there is no saveDir, this page wasn't visited
        if (saveDir == null)
            return;

        // get name of file and path to save directory
        String fileName = link.substring(link.lastIndexOf("/") + 1, link.length());
        String saveFilePath = saveDir + File.separator + fileName;

        String html = Jsoup.connect(link).get().html();

        // TODO: use dfs or kv storage
        // opens an output stream and save new file
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);
        byte[] htmlInBytes = html.getBytes();
        outputStream.write(htmlInBytes, 0, htmlInBytes.length);

        outputStream.close();
    }
}
