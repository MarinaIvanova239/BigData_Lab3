package tests;

import com.spbstu.database.documents.PageContent;
import com.spbstu.database.documents.VisitedPages;
import com.spbstu.database.repositories.PageContentRepository;
import com.spbstu.database.repositories.VisitedPagesRepository;
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
