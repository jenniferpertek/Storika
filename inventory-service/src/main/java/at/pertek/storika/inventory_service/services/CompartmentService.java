package at.pertek.storika.inventory_service.services;

import at.pertek.inventoryservice.commons.exception.EntryNotFoundException;
import at.pertek.inventoryservice.commons.exception.ErrorCode;
import at.pertek.inventoryservice.inventory.entity.Compartment;
import at.pertek.inventoryservice.inventory.repository.CompartmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CompartmentService {

  private final CompartmentRepository compartmentRepository;

  public Compartment getCompartmentEntityById(Long compartmentId) {
    return compartmentRepository
        .findById(compartmentId)
        .orElseThrow(() -> new EntryNotFoundException(ErrorCode.INVALID_ID));
  }

}
