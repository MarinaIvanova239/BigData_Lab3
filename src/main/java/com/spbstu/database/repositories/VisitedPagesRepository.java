package com.spbstu.database.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.spbstu.database.documents.VisitedPages;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VisitedPagesRepository {

    private static final String COLLECTION_NAME = "VisitedPages";

    @Autowired
    MongoTemplate mongoTemplate;

    public void save(VisitedPages visitedPages) {
        mongoTemplate.save(visitedPages, COLLECTION_NAME);
    }

    public void createCollection() {
        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.createCollection(COLLECTION_NAME);
        }
    }

    public void drop() {
        mongoTemplate.dropCollection(COLLECTION_NAME);
    }

    public VisitedPages findByLink(String link) {
        Query query = new Query();
        query.addCriteria(Criteria.where("link").is(link));
        VisitedPages page = mongoTemplate.findOne(query, VisitedPages.class, COLLECTION_NAME);
        return page;
    }
}
