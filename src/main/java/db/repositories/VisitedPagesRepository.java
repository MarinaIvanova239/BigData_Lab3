package java.db.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.db.entities.VisitedPages;
import java.util.List;

public interface VisitedPagesRepository extends MongoRepository<VisitedPages, Integer> {

    List<VisitedPages> findByLink(String link);
}
