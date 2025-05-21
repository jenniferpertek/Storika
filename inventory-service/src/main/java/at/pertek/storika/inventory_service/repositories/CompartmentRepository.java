package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.Compartment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompartmentRepository extends JpaRepository<Compartment, UUID>, JpaSpecificationExecutor<Compartment> {

  List<Compartment> findByStorageUnitId(UUID storageUnitId);

}
