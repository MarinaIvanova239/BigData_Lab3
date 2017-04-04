package tests;

import database.entities.PageContent;
import database.entities.VisitedPages;
import database.repositories.PageContentRepository;
import database.repositories.VisitedPagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Service
public class MongeTestService {

    @Autowired
    private VisitedPagesRepository visitedPagesRepository;
    @Autowired
    private PageContentRepository pageContentRepository;

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
