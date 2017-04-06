package com.spbstu.database;

import com.spbstu.database.entities.PageContent;
import com.spbstu.database.entities.VisitedPages;
import com.spbstu.database.repositories.PageContentRepository;
import com.spbstu.database.repositories.VisitedPagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MongoDbService {

    @Autowired
    private VisitedPagesRepository visitedPagesRepository;
    @Autowired
    private PageContentRepository pageContentRepository;

    @Bean
    public VisitedPagesRepository visitedPagesRepository() {
        return visitedPagesRepository;
    }

    @Bean
    public PageContentRepository pageContantRepository() {
        return pageContentRepository;
    }

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
