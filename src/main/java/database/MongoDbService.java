package database;

import database.entities.PageContent;
import database.entities.VisitedPages;
import database.repositories.PageContentRepository;
import database.repositories.VisitedPagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoDbService {

    @Autowired
    private VisitedPagesRepository visitedPagesRepository;
    @Autowired
    private PageContentRepository pageContentRepository;

    public void reset() {
        visitedPagesRepository.deleteAll();
        pageContentRepository.deleteAll();
    }

    public void contains(PageContent... contents) {
        for (PageContent content: contents) {
            pageContentRepository.save(content);
        }
    }

    public void contains(VisitedPages... pages) {
        for (VisitedPages page: pages) {
            visitedPagesRepository.save(page);
        }
    }

    public boolean containLink(String link) {
        if (visitedPagesRepository.findByLink(link).size() != 0) {
            return true;
        }
        return false;
    }
}
