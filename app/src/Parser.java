package app.src;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;

public class Parser {

    public static void parsePage(String currentLink, List<String> links, List<String> files)
            throws Exception {
        Document doc = Jsoup.connect(currentLink).get();
        // get links and add them to list
        Elements linksAsElements = doc.select("a[href]");
        for (Element eachLink: linksAsElements) {
            links.add(eachLink.toString());
        }
        // TODO: add all links to list
        // get files and add them to list
        Elements filesAsElements = doc.select("link[href]");
        for (Element eachFile: filesAsElements) {
            files.add(currentLink + File.separator + eachFile.toString());
        }
    }
}
