package app.src;

import java.util.HashMap;
import java.util.Map;

public class VisitedPagesController {

    private Map<String, String> visitedPages = new HashMap<String, String>();

    public boolean checkIfLinkWasVisited(String link) {
        return (visitedPages.containsKey(link));
    }

    public void addVisitedLink(String link) {
        visitedPages.put(link, null);
    }
}
