package java.app;

import org.jsoup.Jsoup;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class DownloaderComponent {

    private static final int DOWNLOAD_LIMIT = 100000;
    private static int counter = 0;

    @RabbitListener(queues = "for_downloading")
    public void runDownloader(String message) throws Exception {
        if (counter < DOWNLOAD_LIMIT) {
            downloadFile(message);
            counter++;
        }
    }

    public void downloadFile(String link) throws Exception {

        // get page to download
        String html = Jsoup.connect(link).get().html();

        // TODO: write to mongoDB
    }
}
