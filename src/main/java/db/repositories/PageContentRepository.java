package java.db.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.db.entities.PageContent;

public interface PageContentRepository extends MongoRepository<PageContent, String> {
}
