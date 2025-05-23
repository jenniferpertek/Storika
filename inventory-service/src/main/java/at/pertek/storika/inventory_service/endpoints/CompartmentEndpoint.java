package at.pertek.storika.inventory_service.endpoints;

import at.pertek.storika.inventory_service.dto.CompartmentDto;
import at.pertek.storika.inventory_service.dto.CompartmentPatchDto;
import at.pertek.storika.inventory_service.endpoint.CompartmentApi;
import at.pertek.storika.inventory_service.services.CompartmentService;
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
public class CompartmentEndpoint implements CompartmentApi {

  private final CompartmentService compartmentService;

  /**
   * POST /v1/compartments : Create a new compartment
   *
   * @param compartmentDto (required)
   * @return Compartment created successfully. (status code 201) or Invalid input (status code 400) or Internal Server
   * Error (status code 500)
   */
  @Override
  public ResponseEntity<CompartmentDto> createCompartment(CompartmentDto compartmentDto) {
    log.info("createCompartment request arrived for: {}", compartmentDto.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(compartmentService.createCompartment(compartmentDto));
  }

  /**
   * DELETE /v1/compartment/{compartmentId} : Delete a compartment by ID
   *
   * @param compartmentId ID of the compartment to retrieve (required)
   * @return Compartment deleted successfully. (status code 204) or Compartment not found. (status code 404) or Conflict
   * - Compartment cannot be deleted as it currently contains items. (status code 409) or Internal Server Error (status
   * code 500)
   */
  @Override
  public ResponseEntity<Void> deleteCompartment(UUID compartmentId) {
    log.info("deleteCompartmentById request arrived for: {}", compartmentId);
    compartmentService.deleteCompartment(compartmentId);

    log.info("Compartment with ID {} deleted.", compartmentId);
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /v1/compartments : Get all compartments
   *
   * @param storageUnitId Filter compartments by storage unit ID (optional)
   * @param name Filter compartments by name (optional)
   * @param sortBy Field to sort the compartments by. Allowed values typically include &#39;name&#39;. (optional)
   * @param sortOrder Sort order. (optional, default to asc)
   * @param page Page number of the result set (0-indexed). (optional, default to 0)
   * @param size Number of items to return per page. (optional, default to 10)
   * @return List of compartments. (status code 200) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<List<CompartmentDto>> getAllCompartments(UUID storageUnitId, String name, String sortBy, String sortOrder, Integer page, Integer size) {
    log.info("getAllCompartments request received with parameters - storageUnitId: [{}], name: [{}], sortBy: [{}], sortOrder: [{}], page: [{}], size: [{}]",
        storageUnitId, name, sortBy, sortOrder, page, size);

    Page<CompartmentDto> compartmentsPage = compartmentService.getAllCompartments(storageUnitId, name, sortBy, sortOrder, page, size);

    List<CompartmentDto> compartmentsOnPage = compartmentsPage.getContent();

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("X-Total-Count", String.valueOf(compartmentsPage.getTotalElements()));
    responseHeaders.add("X-Total-Pages", String.valueOf(compartmentsPage.getTotalPages()));
    responseHeaders.add("X-Current-Page", String.valueOf(compartmentsPage.getNumber()));
    responseHeaders.add("X-Page-Size", String.valueOf(compartmentsPage.getSize()));

    log.debug("Returning {} compartments for page {} (size {}). Total items: {}, Total pages: {}",
        compartmentsOnPage.size(), compartmentsPage.getNumber(), compartmentsPage.getSize(), compartmentsPage.getTotalElements(), compartmentsPage.getTotalPages());

    return ResponseEntity.ok().headers(responseHeaders).body(compartmentsOnPage);
  }

  /**
   * GET /v1/compartment/{compartmentId} : Get a compartment by ID
   *
   * @param compartmentId ID of the compartment to retrieve (required)
   * @return Compartment found. (status code 200) or Compartment not found. (status code 404) or Internal Server Error
   * (status code 500)
   */
  @Override
  public ResponseEntity<CompartmentDto> getCompartmentById(UUID compartmentId) {
    log.info("getCompartmentById request arrived with ID: {}", compartmentId);
    return ResponseEntity.ok(compartmentService.getCompartmentById(compartmentId));
  }

  /**
   * PATCH /v1/compartment/{compartmentId} : Partially update a compartment by ID
   *
   * @param compartmentId ID of the compartment to retrieve (required)
   * @param compartmentPatchDto (required)
   * @return Compartment updated successfully. (status code 200) or Invalid input (status code 400) or Compartment not
   * found. (status code 404) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<CompartmentDto> patchCompartment(UUID compartmentId, CompartmentPatchDto compartmentPatchDto) {
    log.info("patchCompartment request arrived for: {}", compartmentId);
    return ResponseEntity.ok(compartmentService.patchCompartment(compartmentId, compartmentPatchDto));
  }

}
