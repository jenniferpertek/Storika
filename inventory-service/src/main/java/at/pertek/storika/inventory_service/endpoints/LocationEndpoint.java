package at.pertek.storika.inventory_service.endpoints;

import at.pertek.storika.inventory_service.dto.LocationDto;
import at.pertek.storika.inventory_service.dto.LocationPatchDto;
import at.pertek.storika.inventory_service.endpoint.LocationApi;
import at.pertek.storika.inventory_service.services.LocationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LocationEndpoint implements LocationApi {

  private final LocationService locationService;

  /**
   * POST /v1/locations : Create a new location
   *
   * @param locationDto (required)
   * @return Location created successfully. (status code 201) or Invalid Input (status code 400) or Internal Server
   * Error (status code 500)
   */
  @Override
  public ResponseEntity<LocationDto> createLocation(LocationDto locationDto) {
    log.info("createLocation request arrived for: {}", locationDto.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(locationDto));
  }

  /**
   * DELETE /v1/location/{locationId} : Delete a location by ID
   *
   * @param locationId The ID of the location to retrieve. (required)
   * @return Location deleted successfully (status code 204) or Location not found (status code 404) or Conflict -
   * Location cannot be deleted as it contains storage units. (status code 409) or Internal Server Error (status code
   * 500)
   */
  @Override
  public ResponseEntity<Void> deleteLocation(UUID locationId) {
    log.info("deleteLocation request arrived for: {}", locationId);
    locationService.deleteLocation(locationId);

    log.info("Location with ID {} deleted.", locationId);
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /v1/locations : Get all locations
   *
   * @param name Filter locations by name. (optional)
   * @param sortBy Field to sort the locations by. Allowed values typically include &#39;name&#39;. (optional, default
   * to name)
   * @param sortOrder Sort order. (optional, default to asc)
   * @param page Page number of the result set (0-indexed). (optional, default to 0)
   * @param size Number of items to return per page. (optional, default to 10)
   * @return A list of locations. (status code 200) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<List<LocationDto>> getAllLocations(String name, String sortBy, String sortOrder, Integer page, Integer size) {
    log.info("getAllLocations request arrived.");
    return ResponseEntity.ok(locationService.getAllLocations(name, sortBy, sortOrder, page, size));
  }

  /**
   * GET /v1/location/{locationId} : Get location by ID
   *
   * @param locationId The ID of the location to retrieve. (required)
   * @return Location found (status code 200) or Location not found (status code 404) or Internal Server Error (status
   * code 500)
   */
  @Override
  public ResponseEntity<LocationDto> getLocationById(UUID locationId) {
    log.info("getLocationById request arrived with ID: {}", locationId);
    return ResponseEntity.ok(locationService.getLocationById(locationId));
  }

  /**
   * PATCH /v1/location/{locationId} : Partially update a location by ID
   *
   * @param locationId The ID of the location to retrieve. (required)
   * @param locationPatchDto (required)
   * @return Location updated successfully (status code 200) or Invalid Input (status code 400) or Location not found
   * (status code 404) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<LocationDto> patchLocation(UUID locationId, LocationPatchDto locationPatchDto) {
    log.info("patchLocation request arrived for: {}", locationId);
    return ResponseEntity.ok(locationService.patchLocation(locationId, locationPatchDto));
  }

}
