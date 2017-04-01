package java.db.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.db.entities.VisitedPages;
import java.util.List;

public interface VisitedPagesRepository extends PagingAndSortingRepository<VisitedPages, Integer> {

    List<VisitedPages> findByLink(String link);
}
