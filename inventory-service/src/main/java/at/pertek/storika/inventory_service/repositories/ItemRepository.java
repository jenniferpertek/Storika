package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.Item;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemRepository extends JpaRepository<Item, UUID>, JpaSpecificationExecutor<Item> {

  List<Item> findByStorageUnitId(UUID storageUnitId);

  List<Item> findByCompartmentId(UUID compartmentId);

  List<Item> findByCategoryId(UUID categoryId);
  
}
