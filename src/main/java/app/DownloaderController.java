package java.app;

import org.jsoup.Jsoup;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.db.entities.PageContent;

@Controller
@EnableRabbit
public class DownloaderController {

    private static final int DOWNLOAD_LIMIT = 100000;
    private static int counter = 0;
    private MongoDatabase database;

    @RabbitListener(queues = "for_downloading")
    public void runDownloader(String message) throws Exception {
        if (counter < DOWNLOAD_LIMIT) {
            downloadFile(message);
            counter++;
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
