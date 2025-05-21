package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.StorageUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StorageUnitRepository extends JpaRepository<StorageUnit, UUID>, JpaSpecificationExecutor<StorageUnit> {

  List<StorageUnit> findByLocationId(UUID locationId);

}
