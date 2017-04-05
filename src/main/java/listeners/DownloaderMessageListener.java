package listeners;

import database.MongoDbService;
import database.entities.PageContent;
import org.jsoup.Jsoup;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

public class DownloaderMessageListener implements MessageListener {

    @Autowired
    private MongoDbService database;

    private static final int DOWNLOAD_LIMIT = 100000;
    private static int counter = 0;

    @Override
    public void onMessage(Message message) {
        try {
            String link = new String(message.getBody(), "UTF-8");
            //System.out.println("Received: " + link);
            if (counter < DOWNLOAD_LIMIT) {
                downloadFile(link);
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String link) throws Exception {
        // get content of file
        String htmlContent = Jsoup.connect(link).get().html();
        // save it in database
        PageContent content = new PageContent(link, htmlContent);
        database.contains(content);
    }
}
