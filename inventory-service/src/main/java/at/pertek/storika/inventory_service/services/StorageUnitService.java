package at.pertek.storika.inventory_service.services;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.StorageUnitDto;
import at.pertek.storika.inventory_service.entities.Location;
import at.pertek.storika.inventory_service.entities.StorageUnit;
import at.pertek.storika.inventory_service.mappers.StorageUnitMapper;
import at.pertek.storika.inventory_service.repositories.StorageUnitRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class StorageUnitService {

  private final StorageUnitRepository storageUnitRepository;
  private final StorageUnitMapper storageUnitMapper;
  private final LocationService locationService;

  @Transactional(readOnly = true)
  public List<StorageUnitDto> getAllStorageUnits() {
    log.debug("Fetching all storage units");

    return storageUnitRepository
        .findAll()
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
  public StorageUnitDto updateStorageUnit(UUID storageUnitId, StorageUnitDto storageUnitDto) {
    log.info("Updating storage unit with id: {}", storageUnitId);

    StorageUnit existingStorageUnit = getStorageUnitEntityById(storageUnitId);

    if (storageUnitDto.getLocationId() != null
        && (existingStorageUnit.getLocation() == null ||
        !existingStorageUnit.getLocation().getLocationId().equals(storageUnitDto.getLocationId()))) {
      log.debug("Updating location for storage unit id: {}", storageUnitId);
      Location newLocation = locationService.getLocationEntityById(storageUnitDto.getLocationId());
      existingStorageUnit.setLocation(newLocation);
    }

    storageUnitMapper.updateStorageUnitFromDto(storageUnitDto, existingStorageUnit);

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

    return storageUnitRepository.findByLocationId(locationId)
        .stream()
        .map(storageUnitMapper::entityToDto)
        .toList();
  }

}
