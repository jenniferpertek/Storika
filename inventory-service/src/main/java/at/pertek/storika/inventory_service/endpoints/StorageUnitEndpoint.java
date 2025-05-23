package at.pertek.storika.inventory_service.endpoints;

import at.pertek.storika.inventory_service.dto.StorageUnitDto;
import at.pertek.storika.inventory_service.dto.StorageUnitPatchDto;
import at.pertek.storika.inventory_service.endpoint.StorageUnitApi;
import at.pertek.storika.inventory_service.services.StorageUnitService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StorageUnitEndpoint implements StorageUnitApi {

  private final StorageUnitService storageUnitService;

  /**
   * POST /v1/storage-units : Create a new storage unit
   *
   * @param storageUnitDto (required)
   * @return Storage unit created successfully. (status code 201) or Invalid input (status code 400) or Internal Server
   * Error (status code 500)
   */
  @Override
  public ResponseEntity<StorageUnitDto> createStorageUnit(StorageUnitDto storageUnitDto) {
    log.info("createStorageUnit request arrived for: {}", storageUnitDto.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(storageUnitService.createStorageUnit(storageUnitDto));
  }

  /**
   * DELETE /v1/storage-unit/{storageUnitId} : Delete a storage unit by ID
   *
   * @param storageUnitId ID of the storage unit (required)
   * @return Storage unit deleted successfully. (status code 204) or Storage unit not found (status code 404) or
   * Conflict - Storage unit cannot be deleted as it contains items. (status code 409) or Internal Server Error (status
   * code 500)
   */
  @Override
  public ResponseEntity<Void> deleteStorageUnit(UUID storageUnitId) {
    log.info("deleteStorageUnit request arrived for: {}", storageUnitId);
    storageUnitService.deleteStorageUnit(storageUnitId);

    log.info("StorageUnit with ID {} deleted.", storageUnitId);
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /v1/storage-units : Get all storage units
   *
   * @param locationId Filter storage units by locationId (optional)
   * @param name Filter storage units by name (optional)
   * @param sortBy Field to sort the storage units by. Allowed values typically include &#39;name&#39;. (optional)
   * @param sortOrder Sort order. (optional, default to asc)
   * @param page Page number of the result set (0-indexed). (optional, default to 0)
   * @param size Number of items to return per page. (optional, default to 10)
   * @return List of storage units. (status code 200) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<List<StorageUnitDto>> getAllStorageUnits(UUID locationId, String name, String sortBy,
                                                                 String sortOrder, Integer page, Integer size) {
    log.info("getAllStorageUnits request received with parameters - locationId: [{}], name: [{}], sortBy: [{}], " +
            "sortOrder: [{}], page: [{}], size: [{}]", locationId, name, sortBy, sortOrder, page, size);

    Page<StorageUnitDto> storageUnitsPage = storageUnitService.getAllStorageUnits(
        locationId, name, sortBy, sortOrder, page, size
    );

    List<StorageUnitDto> storageUnitsOnPage = storageUnitsPage.getContent();

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("X-Total-Count", String.valueOf(storageUnitsPage.getTotalElements()));
    responseHeaders.add("X-Total-Pages", String.valueOf(storageUnitsPage.getTotalPages()));
    responseHeaders.add("X-Current-Page", String.valueOf(storageUnitsPage.getNumber()));
    responseHeaders.add("X-Page-Size", String.valueOf(storageUnitsPage.getSize()));

    log.debug("Returning {} storageUnits for page {} (size {}). Total items: {}, Total pages: {}",
        storageUnitsOnPage.size(), storageUnitsPage.getNumber(), storageUnitsPage.getSize(),
        storageUnitsPage.getTotalElements(), storageUnitsPage.getTotalPages());

    return ResponseEntity.ok().headers(responseHeaders).body(storageUnitsOnPage);
  }

  /**
   * GET /v1/storage-unit/{storageUnitId} : Get a storage unit by ID
   *
   * @param storageUnitId ID of the storage unit (required)
   * @return Storage unit found. (status code 200) or Storage unit not found (status code 404) or Internal Server Error
   * (status code 500)
   */
  @Override
  public ResponseEntity<StorageUnitDto> getStorageUnitById(UUID storageUnitId) {
    log.info("getStorageUnitById request arrived with ID: {}", storageUnitId);
    return ResponseEntity.ok(storageUnitService.getStorageUnitById(storageUnitId));
  }

  /**
   * PATCH /v1/storage-unit/{storageUnitId} : Partially update a storage unit by ID
   *
   * @param storageUnitId ID of the storage unit (required)
   * @param storageUnitPatchDto (required)
   * @return Storage unit updated successfully. (status code 200) or Invalid input (status code 400) or Storage unit not
   * found (status code 404) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<StorageUnitDto> patchStorageUnit(UUID storageUnitId, StorageUnitPatchDto storageUnitPatchDto) {
    log.info("patchStorageUnit request arrived for: {}", storageUnitId);
    return ResponseEntity.ok(storageUnitService.patchStorageUnit(storageUnitId, storageUnitPatchDto));
  }

}
