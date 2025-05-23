package at.pertek.storika.inventory_service.services;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.LocationDto;
import at.pertek.storika.inventory_service.dto.LocationPatchDto;
import at.pertek.storika.inventory_service.entities.Location;
import at.pertek.storika.inventory_service.mappers.LocationMapper;
import at.pertek.storika.inventory_service.repositories.LocationRepository;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class LocationService {

  private final LocationRepository locationRepository;
  private final LocationMapper locationMapper;

  private static final String DEFAULT_LOCATION_SORT_FIELD = "name";
  private static final Set<String> ALLOWED_LOCATION_SORT_FIELDS = Set.of(
      DEFAULT_LOCATION_SORT_FIELD
  );


  @Transactional(readOnly = true)
  public Page<LocationDto> getAllLocations(String name, String sortBy, String sortOrder, Integer page, Integer size) {
    log.debug("Fetching locations with filters - name: [{}], sortBy: [{}], sortOrder: [{}], page: [{}], size: [{}]",
        name, sortBy, sortOrder, page, size);

    Pageable pageable = PaginationUtil.createPageable(
        page,
        size,
        sortBy,
        sortOrder,
        DEFAULT_LOCATION_SORT_FIELD,
        ALLOWED_LOCATION_SORT_FIELDS
    );

    log.debug("Constructed Pageable: {}", pageable);

    Page<Location> locationPage = locationRepository.findByOptionalFilters(
        name, pageable
    );

    return locationPage.map(locationMapper::entityToDto);
  }

  @Transactional(readOnly = true)
  public Location getLocationEntityById(UUID locationId) {
    log.debug("Fetching location entity with id: {}", locationId);

    return locationRepository
        .findById(locationId)
        .orElseThrow(() -> {
          log.warn("Location not found with id: {}", locationId);
          return new EntryNotFoundException(ErrorCode.INVALID_ID);
        });
  }

  @Transactional(readOnly = true)
  public LocationDto getLocationById(UUID locationId) {
    log.debug("Fetching location DTO with id: {}", locationId);

    return locationMapper.entityToDto(getLocationEntityById(locationId));
  }

  @Transactional
  public LocationDto createLocation(LocationDto locationDto) {
    log.info("Creating new location with name: {}", locationDto.getName());

    Location location = locationMapper.dtoToEntity(locationDto);
    Location savedLocation = locationRepository.save(location);

    log.info("Successfully created location with id: {}", savedLocation.getLocationId());

    return locationMapper.entityToDto(savedLocation);
  }

  @Transactional
  public LocationDto patchLocation(UUID locationId, LocationPatchDto locationpatchDto) {
    log.info("Updating location with id: {}", locationId);

    Location existingLocation = getLocationEntityById(locationId);
    locationMapper.patchLocationFromDto(locationpatchDto, existingLocation);
    Location updatedLocation = locationRepository.save(existingLocation);

    log.info("Successfully updated location with id: {}", updatedLocation.getLocationId());

    return locationMapper.entityToDto(updatedLocation);
  }

  @Transactional
  public void deleteLocation(UUID locationId) {
    log.info("Deleting location with id: {}", locationId);

    Location location = getLocationEntityById(locationId);
    locationRepository.delete(location);

    log.info("Successfully deleted location with id: {}", locationId);
  }

}
