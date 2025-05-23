package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.Compartment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompartmentRepository extends JpaRepository<Compartment, UUID>, JpaSpecificationExecutor<Compartment> {

  List<Compartment> findByStorageUnitId(UUID storageUnitId);

  /**
   * Finds compartments based on optional filters for storage unit ID and name,
   * and applies specified sorting.
   * The name search is case-insensitive and allows partial matches.
   *
   * @param storageUnitId Optional ID of the storage unit to filter by.
   * @param name Optional name (or part of it) of the compartment to filter by.
   * @param pageable  {@link Pageable} object containing pagination (page number, page size) and sorting information.
   * @return A list of matching compartments, sorted as specified.
   */
  @Query("SELECT c FROM Compartment c WHERE " +
      "(:storageUnitId IS NULL OR c.storageUnit.storageUnitId = :storageUnitId) AND " +
      "(:name IS NULL OR :name = '' OR lower(c.name) LIKE lower(concat('%', :name, '%')))")
  Page<Compartment> findByOptionalFilters(
      @Param("storageUnitId") UUID storageUnitId,
      @Param("name") String name,
      Pageable pageable
  );

}
