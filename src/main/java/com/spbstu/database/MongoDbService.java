package com.spbstu.database;

import com.spbstu.database.documents.PageContent;
import com.spbstu.database.documents.VisitedPages;
import com.spbstu.database.repositories.PageContentRepository;
import com.spbstu.database.repositories.VisitedPagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoDbService {

    @Autowired
    private VisitedPagesRepository visitedPagesRepository;
    @Autowired
    private PageContentRepository pageContentRepository;

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
}
