package database.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import database.entities.PageContent;

public interface PageContentRepository extends MongoRepository<PageContent, String> {
}
