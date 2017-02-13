package app.src;

import java.util.HashMap;
import java.util.Map;

public class VisitedPagesController {

    private int numberVisitedPages = 0;
    private Map<String, String> visitedPages = new HashMap<String, String>();
    private final String dirName = "dir-";

    public boolean checkIfLinkWasVisited(String link) {
        return visitedPages.containsKey(link);
    }

    public synchronized void addVisitedLink(String link) {
        visitedPages.put(link, dirName + String.valueOf(numberVisitedPages));
        numberVisitedPages++;
    }

    public String getVisitedLinkDir(String link) {
        String parentLink = link.substring(0, link.lastIndexOf("/") - 1);
        if (visitedPages.containsKey(link)) {
            return visitedPages.get(link);
        } else if (visitedPages.containsKey(parentLink)) {
            return visitedPages.get(parentLink);
        }
        return null;
    }
}
