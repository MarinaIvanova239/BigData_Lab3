package com.spbstu.database.repositories;

import com.spbstu.database.documents.PageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class PageContentRepository {

    private static final String COLLECTION_NAME = "PageContent";

    @Autowired
    MongoTemplate mongoTemplate;

    public void save(PageContent pageContent) {
        mongoTemplate.save(pageContent, COLLECTION_NAME);
    }

    public void createCollection() {
        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.createCollection(COLLECTION_NAME);
        }
    }

    public void drop() {
        mongoTemplate.dropCollection(COLLECTION_NAME);
    }

    public PageContent findByLink(String link) {
        Query query = new Query();
        query.addCriteria(Criteria.where("link").is(link));
        PageContent page = mongoTemplate.findOne(query, PageContent.class, COLLECTION_NAME);
        return page;
    }
}
