package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.Item;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, UUID>, JpaSpecificationExecutor<Item> {

  List<Item> findByStorageUnit_StorageUnitId(UUID storageUnitId);

  List<Item> findByCompartment_CompartmentId(UUID compartmentId);

  List<Item> findByCategory_CategoryId(UUID categoryId);

  /**
   * Finds items based on a set of optional filter criteria, with pagination and sorting.
   * <p>
   * The query supports filtering by:
   * <ul>
   *   <li>Category ID</li>
   *   <li>Storage Unit ID</li>
   *   <li>Compartment ID</li>
   *   <li>Quantity (items with quantity greater than or equal to the provided value)</li>
   *   <li>Name (case-insensitive partial match)</li>
   *   <li>Expiration status (expired or not expired)</li>
   * </ul>
   * If a filter parameter is null, it is not applied.
   *
   * @param categoryId Optional UUID of the category to filter by.
   * @param storageUnitId Optional UUID of the storage unit to filter by.
   * @param compartmentId Optional UUID of the compartment to filter by.
   * @param quantity Optional minimum quantity to filter by.
   * @param isExpired Optional boolean to filter by expiration status.
   *                  If true, finds expired items. If false, finds non-expired items (including those with no expiration date).
   * @param name Optional string for a case-insensitive partial match on the item's name.
   * @param pageable  {@link Pageable} object containing pagination (page number, page size) and sorting information.
   * @return A {@link Page} of {@link Item} entities matching the filter criteria,
   *         along with pagination information.
   */
  @Query("SELECT i FROM Item i WHERE " +
      "(:categoryId IS NULL OR i.category.id = :categoryId) AND " +
      "(:storageUnitId IS NULL OR i.storageUnit.id = :storageUnitId) AND " +
      "(:compartmentId IS NULL OR i.compartment.id = :compartmentId) AND " +
      "(:quantity IS NULL OR i.quantity >= :quantity) AND " +
      "(:name IS NULL OR :name = '' OR lower(i.name) LIKE lower(concat('%', :name, '%'))) AND " +
      "(:isExpired IS NULL OR " +
      "    (:isExpired = true AND i.expirationDate IS NOT NULL AND i.expirationDate < CURRENT_DATE) OR " +
      "    (:isExpired = false AND (i.expirationDate IS NULL OR i.expirationDate >= CURRENT_DATE))" +
      ")")
  Page<Item> findByOptionalFilters(
      @Param("categoryId") UUID categoryId,
      @Param("storageUnitId") UUID storageUnitId,
      @Param("compartmentId") UUID compartmentId,
      @Param("quantity") Float quantity,
      @Param("isExpired") Boolean isExpired,
      @Param("name") String name,
      Pageable pageable
  );
  
}
