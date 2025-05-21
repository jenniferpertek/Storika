package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.StorageUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StorageUnitRepository extends JpaRepository<StorageUnit, UUID>, JpaSpecificationExecutor<StorageUnit> {

  /**
   * Finds storage units based on optional filters for location ID and name.
   * The name search is case-insensitive and allows partial matches.
   *
   * @param locationId Optional ID of the location to filter by.
   * @param name Optional name (or part of it) of the storage unit to filter by.
   * @param sort The sorting parameters to apply.
   * @return A list of matching storage units.
   */
  @Query("SELECT su FROM StorageUnit su WHERE " +
      "(:locationId IS NULL OR su.location.id = :locationId) AND " +
      "(:name IS NULL OR :name = '' OR lower(su.name) LIKE lower(concat('%', :name, '%')))")
  List<StorageUnit> findByOptionalFilters(
      @Param("locationId") UUID locationId,
      @Param("name") String name,
      Sort sort
  );

}
