package at.pertek.storika.inventory_service.services;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.CompartmentDto;
import at.pertek.storika.inventory_service.dto.CompartmentPatchDto;
import at.pertek.storika.inventory_service.entities.Compartment;
import at.pertek.storika.inventory_service.entities.StorageUnit;
import at.pertek.storika.inventory_service.mappers.CompartmentMapper;
import at.pertek.storika.inventory_service.repositories.CompartmentRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class CompartmentService {

  private final CompartmentRepository compartmentRepository;
  private final CompartmentMapper compartmentMapper;
  private final StorageUnitService storageUnitService;

  private static final String DEFAULT_COMPARTMENT_SORT_FIELD = "name";
  private static final Set<String> ALLOWED_COMPARTMENT_SORT_FIELDS = Set.of(
      DEFAULT_COMPARTMENT_SORT_FIELD, "storageUnitId"
  );

  @Transactional(readOnly = true)
  public Page<CompartmentDto> getAllCompartments(UUID storageUnitId, String name, String sortBy, String sortOrder, Integer page, Integer size) {
    log.debug("Fetching compartments with filters - storageUnitId: [{}], name: [{}], sortBy: [{}], sortOrder: [{}], page: [{}], size: [{}]",
        storageUnitId, name, sortBy, sortOrder, page, size);

    Pageable pageable = PaginationUtil.createPageable(
        page,
        size,
        sortBy,
        sortOrder,
        DEFAULT_COMPARTMENT_SORT_FIELD,
        ALLOWED_COMPARTMENT_SORT_FIELDS
    );

    log.debug("Constructed Pageable: {}", pageable);

    Page<Compartment> compartmentPage = compartmentRepository.findByOptionalFilters(
        storageUnitId, name, pageable
    );

    return compartmentPage.map(compartmentMapper::entityToDto);
  }

  @Transactional(readOnly = true)
  public Compartment getCompartmentEntityById(UUID compartmentId) {
    log.debug("Fetching compartment entity with id: {}", compartmentId);

    return compartmentRepository
        .findById(compartmentId)
        .orElseThrow(() -> {
          log.warn("Compartment not found with id: {}", compartmentId);
          return new EntryNotFoundException(ErrorCode.INVALID_ID);
        });
  }

  @Transactional
  public CompartmentDto getCompartmentById(UUID compartmentId) {
    log.debug("Fetching category DTO with id: {}", compartmentId);

    return compartmentMapper.entityToDto(getCompartmentEntityById(compartmentId));
  }

  @Transactional
  public CompartmentDto createCompartment(CompartmentDto compartmentDto) {
    log.info("Creating new compartment with name: {}", compartmentDto.getName());

    if (compartmentDto.getStorageUnitId() == null) {
      log.error("StorageUnitId is required to create a compartment.");
      throw new IllegalArgumentException("StorageUnitId cannot be null when creating a compartment.");
    }

    StorageUnit storageUnit = storageUnitService.getStorageUnitEntityById(compartmentDto.getStorageUnitId());

    Compartment compartment = compartmentMapper.dtoToEntity(compartmentDto);
    compartment.setStorageUnit(storageUnit);

    Compartment savedCompartment = compartmentRepository.save(compartment);

    log.info("Successfully created compartment with id: {}", savedCompartment.getStorageUnit());

    return compartmentMapper.entityToDto(savedCompartment);
  }

  @Transactional
  public CompartmentDto patchCompartment(UUID compartmentId, CompartmentPatchDto compartmentPatchDto) {
    log.info("Updating compartment with id: {}", compartmentId);

    Compartment existingCompartment = getCompartmentEntityById(compartmentId);

    JsonNullable<UUID> storageUnitIdFromPatch = compartmentPatchDto.getStorageUnitId();

    if (storageUnitIdFromPatch != null && storageUnitIdFromPatch.isPresent()) {
      UUID newActualStorageUnitId = storageUnitIdFromPatch.get();

      if (newActualStorageUnitId != null) {
        if (existingCompartment.getStorageUnit() == null ||
            !newActualStorageUnitId.equals(existingCompartment.getStorageUnit().getStorageUnitId())) {
          log.debug("Updating storage unit for compartment id: {} to new storageUnitId: {}", compartmentId, newActualStorageUnitId);
          StorageUnit newStorageUnit = storageUnitService.getStorageUnitEntityById(newActualStorageUnitId);
          existingCompartment.setStorageUnit(newStorageUnit);
        }
      } else {
        if (existingCompartment.getStorageUnit() != null) {
          log.debug("Disassociating storage unit for compartment id: {}", compartmentId);
          existingCompartment.setStorageUnit(null);
        }
      }
    }

    compartmentMapper.patchCompartmentFromDto(compartmentPatchDto, existingCompartment);

    Compartment updatedCompartment = compartmentRepository.save(existingCompartment);

    log.info("Successfully updated compartment with id: {}", updatedCompartment.getCompartmentId());

    return compartmentMapper.entityToDto(updatedCompartment);
  }

  @Transactional
  public void deleteCompartment(UUID compartmentId) {
    log.info("Deleting compartment with id: {}", compartmentId);

    Compartment compartment = getCompartmentEntityById(compartmentId);
    compartmentRepository.delete(compartment);

    log.info("Successfully deleted compartment with id: {}", compartmentId);
  }

  @Transactional(readOnly = true)
  public List<CompartmentDto> getCompartmentsByStorageUnitId(UUID storageUnitId) {
    log.debug("Fetching compartments for storage unit id: {}", storageUnitId);

    storageUnitService.getStorageUnitEntityById(storageUnitId);

    return compartmentRepository.findByStorageUnit_StorageUnitId(storageUnitId)
        .stream()
        .map(compartmentMapper::entityToDto)
        .toList();
  }

}
