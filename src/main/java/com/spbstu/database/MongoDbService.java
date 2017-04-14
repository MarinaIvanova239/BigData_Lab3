package com.spbstu.database;

import com.spbstu.database.documents.PageContent;
import com.spbstu.database.documents.VisitedPages;
import com.spbstu.database.repositories.PageContentRepository;
import com.spbstu.database.repositories.VisitedPagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Service
public class MongoDbService {

    @Autowired
    private VisitedPagesRepository visitedPagesRepository;
    @Autowired
    private PageContentRepository pageContentRepository;

    public void reset() {
        visitedPagesRepository.drop();
        pageContentRepository.drop();
        createCollections();
    }

    public void createCollections() {
        pageContentRepository.createCollection();
        visitedPagesRepository.createCollection();
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
        if (visitedPagesRepository.findByLink(link) != null) {
            return true;
        }
        return false;
    }

    public void shouldContain(PageContent... contents) {
        for (PageContent content: contents) {
            PageContent actualContent = pageContentRepository.findByLink(content.getLink());
            assertNotEquals(actualContent, null);
            assertEquals(actualContent.getContent(), content.getContent());
        }
    }

    public void shouldContain(String... links) {
        for (int i = 0; i < links.length; i++) {
            VisitedPages page = visitedPagesRepository.findByLink(links[i]);
            assertNotEquals(page, null);
        }
    }
}
