package app.src;

import com.rabbitmq.client.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;

public class Downloader {

    private static final int DOWNLOAD_LIMIT = 100000;

    private String rabbitmqHost;
    private Channel rabbitmqChannel;
    private VisitedPagesController controller;

    // file system
    private FileSystem hdfs;
    private Path hdfsHomeDir = null;

    Downloader(String rabbitmqHost, VisitedPagesController controller) {
        this.rabbitmqHost = rabbitmqHost;
        this.controller = controller;
    }

    public void run() throws Exception {
        // init connection with hdfs system
        hdfs = FileSystem.get(new Configuration());
        hdfsHomeDir = hdfs.getHomeDirectory();

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
            private int depth = 0;

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    if (depth >= DOWNLOAD_LIMIT) {
                        getChannel().close();
                    }
                    String dir = controller.getVisitedLinkDir(message);
                    downloadFile(message, dir);
                    depth++;
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

        // file cannot be downloaded if one of dirs is null
        if (saveDir == null || hdfsHomeDir == null)
            return;

        // get name of file and path to save directory
        String fileName = link.substring(link.lastIndexOf("/") + 1, link.length());
        String saveFilePath = hdfsHomeDir.toString() + File.separator + saveDir + File.separator + fileName;

        // create directory if it doesn't exist
        Path newFolderPath = new Path(hdfsHomeDir.toString() + File.separator + saveDir);
        if (hdfs.exists(newFolderPath)) {
            hdfs.mkdirs(newFolderPath);
        }

        // get page to download
        // TODO: check how to get not html files
        String html = Jsoup.connect(link).get().html();
        byte[] htmlInBytes = html.getBytes();

        // opens an output stream and save new file
        FSDataOutputStream fsOutStream = hdfs.create(new Path(saveFilePath));
        fsOutStream.write(htmlInBytes);
        fsOutStream.close();
    }
}
