package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.StorageUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StorageUnitRepository extends JpaRepository<StorageUnit, Long>, JpaSpecificationExecutor<StorageUnit> {
}
