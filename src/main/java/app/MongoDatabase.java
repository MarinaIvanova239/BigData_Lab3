package java.app;

import org.springframework.beans.factory.annotation.Autowired;

import java.db.entities.PageContent;
import java.db.entities.VisitedPages;
import java.db.repositories.PageContentRepository;
import java.db.repositories.VisitedPagesRepository;
import java.tests.DbContent;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MongoDatabase {

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

    public void shouldContain(DbContent... contents) {
        for (DbContent content: contents) {
            PageContent actualContent = pageContentRepository.findOne(content.getLink());
            assertNotEquals(actualContent, null);
            assertEquals(actualContent.getContent(), content.getContent());
        }
    }

    public void shouldContain(String... links) {
        for (int i = 0; i < links.length; i++) {
            List<VisitedPages> pages = visitedPagesRepository.findByLink(links[i]);
            assertNotEquals(pages, null);
            assertNotEquals(pages.size(), 0);
        }
    }
}
