package at.pertek.storika.inventory_service.repositories;

import at.pertek.storika.inventory_service.entities.Compartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompartmentRepository extends JpaRepository<Compartment, Long>, JpaSpecificationExecutor<Compartment> {
}
