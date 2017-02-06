package app.src;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Downloader {

    private static final int DOWNLOAD_LIMIT = 100000;

    private String rabbitmqHost;
    private Channel rabbitmqChannel;

    Downloader(String rabbitmqHost) {
        this.rabbitmqHost = rabbitmqHost;
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
                    downloadFile(message);
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

    public void downloadFile(String fileName) {
        // TODO: download file
    }
}
