package at.pertek.storika.inventory_service.services;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.StorageUnitDto;
import at.pertek.storika.inventory_service.dto.StorageUnitPatchDto;
import at.pertek.storika.inventory_service.entities.Location;
import at.pertek.storika.inventory_service.entities.StorageUnit;
import at.pertek.storika.inventory_service.mappers.StorageUnitMapper;
import at.pertek.storika.inventory_service.repositories.StorageUnitRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@AllArgsConstructor
@Service
public class StorageUnitService {

  private final StorageUnitRepository storageUnitRepository;
  private final StorageUnitMapper storageUnitMapper;
  private final LocationService locationService;

  @Transactional(readOnly = true)
  public List<StorageUnitDto> getAllStorageUnits(UUID locationId, String name,String sortBy, String sortOrder, Integer page, Integer size) {
    log.debug("Fetching all storage units");

    Sort sort = Sort.unsorted();
    if (StringUtils.hasText(sortBy)) {
      Sort.Direction direction = StringUtils.hasText(sortOrder) && "desc".equalsIgnoreCase(sortOrder) ?
          Sort.Direction.DESC : Sort.Direction.ASC;
      sort = Sort.by(direction, sortBy);
    }

    return storageUnitRepository
        .findByOptionalFilters(locationId, name, sort)
        .stream()
        .map(storageUnitMapper::entityToDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public StorageUnit getStorageUnitEntityById(UUID storageUnitId) {
    log.debug("Fetching storage unit entity with id: {}", storageUnitId);

    return storageUnitRepository
        .findById(storageUnitId)
        .orElseThrow(() -> {
          log.warn("Storage unit not found with id: {}", storageUnitId);
          return new EntryNotFoundException(ErrorCode.INVALID_ID);
        });
  }

  @Transactional(readOnly = true)
  public StorageUnitDto getStorageUnitById(UUID storageUnitId) {
    log.debug("Fetching storage unit DTO with id: {}", storageUnitId);

    return storageUnitMapper.entityToDto(getStorageUnitEntityById(storageUnitId));
  }

  @Transactional
  public StorageUnitDto createStorageUnit(StorageUnitDto storageUnitDto) {
    log.info("Creating new storage unit with name: {}", storageUnitDto.getName());

    if (storageUnitDto.getLocationId() == null) {
      log.error("LocationId is required to create a storage unit.");
      throw new IllegalArgumentException("LocationId cannot be null when creating a storage unit.");
    }

    Location location = locationService.getLocationEntityById(storageUnitDto.getLocationId());

    StorageUnit storageUnit = storageUnitMapper.dtoToEntity(storageUnitDto);
    storageUnit.setLocation(location);
    StorageUnit savedStorageUnit = storageUnitRepository.save(storageUnit);

    log.info("Successfully created storage unit with id: {}", savedStorageUnit.getStorageUnitId());

    return storageUnitMapper.entityToDto(savedStorageUnit);
  }

  @Transactional
  public StorageUnitDto patchStorageUnit(UUID storageUnitId, StorageUnitPatchDto storageUnitPatchDto) {
    log.info("Updating storage unit with id: {}", storageUnitId);

    StorageUnit existingStorageUnit = getStorageUnitEntityById(storageUnitId);

    JsonNullable<UUID> locationIdFromPatch = storageUnitPatchDto.getLocationId();

    if (locationIdFromPatch != null && locationIdFromPatch.isPresent()) {
      UUID newActualLocationId = locationIdFromPatch.get();

      if (newActualLocationId != null) {
         if (existingStorageUnit.getLocation() == null ||
            !newActualLocationId.equals(existingStorageUnit.getLocation().getLocationId())) {
          log.debug("Updating location for storage unit id: {} to new locationId: {}", storageUnitId, newActualLocationId);
          Location newLocation = locationService.getLocationEntityById(newActualLocationId);
          existingStorageUnit.setLocation(newLocation);
        }
      } else {
        if (existingStorageUnit.getLocation() != null) {
          log.debug("Disassociating location for storage unit id: {}", storageUnitId);
          existingStorageUnit.setLocation(null);
        }
      }
    }

    storageUnitMapper.patchStorageUnitFromDto(storageUnitPatchDto, existingStorageUnit);

    StorageUnit updatedStorageUnit = storageUnitRepository.save(existingStorageUnit);

    log.info("Successfully updated storage unit with id: {}", updatedStorageUnit.getStorageUnitId());

    return storageUnitMapper.entityToDto(updatedStorageUnit);
  }

  @Transactional
  public void deleteStorageUnit(UUID storageUnitId) {
    log.info("Deleting storage unit with id: {}", storageUnitId);

    StorageUnit storageUnit = getStorageUnitEntityById(storageUnitId);

    storageUnitRepository.delete(storageUnit);
    log.info("Successfully deleted storage unit with id: {}", storageUnitId);
  }

  @Transactional(readOnly = true)
  public List<StorageUnitDto> getStorageUnitsByLocationId(UUID locationId) {
    log.debug("Fetching storage units for location id: {}", locationId);

    locationService.getLocationEntityById(locationId);

    return storageUnitRepository.findByOptionalFilters(locationId, null, null)
        .stream()
        .map(storageUnitMapper::entityToDto)
        .toList();
  }

}
