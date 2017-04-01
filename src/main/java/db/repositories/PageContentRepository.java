package java.db.repositories;


import org.springframework.data.repository.PagingAndSortingRepository;

import java.db.entities.PageContent;

public interface PageContentRepository extends PagingAndSortingRepository<PageContent, Integer> {
}
