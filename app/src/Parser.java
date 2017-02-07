package app.src;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class Parser {

    public static void parsePage(String currentLink, List<String> links, List<String> files)
            throws Exception {
        Document doc = Jsoup.connect(currentLink).get();
        // get links and add them to list
        Elements linksAsElements = doc.select("body a");
        for (Element eachLink: linksAsElements) {
            links.add(eachLink.toString());
        }
        // get files and add them to list
        // TODO:
    }
}
