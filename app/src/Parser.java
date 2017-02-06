package app.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class Parser {

    public static String getPage(String link) throws Exception {
        // create url from link
        URL page = new URL(link);
        // get page and read it in string
        BufferedReader buffer = new BufferedReader(new InputStreamReader(page.openStream()));
        String line = buffer.readLine();
        StringBuilder builder = new StringBuilder();
        while(line != null) {
            builder.append(line).append("\n");
            line = buffer.readLine();
        }
        buffer.close();
        return builder.toString();
    }

    public static void parsePage(String fileName, List<String> links, List<String> files) {
        // TODO: find link and files in html file and put to list
    }
}
