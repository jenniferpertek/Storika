package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.Category;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {

  /**
   * Finds categories, optionally filtering by name and applying specified sorting.
   * If the name parameter is null or empty, all categories are returned (subject to sorting).
   * The name search is case-insensitive and allows partial matches.
   *
   * @param name Optional name (or part of it) of the category to filter by.
   * @param pageable  {@link Pageable} object containing pagination (page number, page size) and sorting information.
   * @return A list of matching categories, sorted as specified.
   */
  @Query("SELECT c FROM Category c WHERE " +
      "(:name IS NULL OR :name = '' OR lower(c.name) LIKE lower(concat('%', :name, '%')))")
  Page<Category> findByOptionalFilters(@Param("name") String name, Pageable pageable);

}
