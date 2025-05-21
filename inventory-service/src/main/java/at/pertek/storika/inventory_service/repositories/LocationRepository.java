package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.Location;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, UUID>, JpaSpecificationExecutor<Location> {

 /**
  * Finds locations, optionally filtering by name and applying specified sorting.
  * If the name parameter is null or empty, all locations are returned (subject to sorting).
  * The name search is case-insensitive and allows partial matches.
  *
  * @param name Optional name (or part of it) of the location to filter by.
  * @param sort The sorting parameters to apply.
  * @return A list of matching locations, sorted as specified.
  */
 @Query("SELECT l FROM Location l WHERE " +
     "(:name IS NULL OR :name = '' OR lower(l.name) LIKE lower(concat('%', :name, '%')))")
 List<Location> findByOptionalFilters(@Param("name") String name, Sort sort);

}
