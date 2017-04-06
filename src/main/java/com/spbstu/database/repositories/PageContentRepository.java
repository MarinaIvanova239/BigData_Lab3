package com.spbstu.database.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.spbstu.database.entities.PageContent;

public interface PageContentRepository extends MongoRepository<PageContent, String> {
}
