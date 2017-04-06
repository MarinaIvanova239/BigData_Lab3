package com.spbstu.database.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.spbstu.database.entities.VisitedPages;
import java.util.List;

public interface VisitedPagesRepository extends MongoRepository<VisitedPages, Integer> {

    List<VisitedPages> findByLink(String link);
}
