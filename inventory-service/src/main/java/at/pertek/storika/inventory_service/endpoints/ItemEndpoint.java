package at.pertek.storika.inventory_service.endpoints;

import at.pertek.storika.inventory_service.dto.ItemDto;
import at.pertek.storika.inventory_service.dto.ItemPatchDto;
import at.pertek.storika.inventory_service.endpoint.ItemApi;
import at.pertek.storika.inventory_service.services.ItemService;
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
public class ItemEndpoint implements ItemApi {

  private final ItemService itemService;

  /**
   * POST /v1/items : Create a new Item
   *
   * @param itemDto (required)
   * @return Item created successfully. (status code 201) or Invalid input (status code 400) or Internal Server Error
   * (status code 500)
   */
  @Override
  public ResponseEntity<ItemDto> createItem(ItemDto itemDto) {
    log.info("createItem request arrived for: {}", itemDto.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(itemDto));
  }

  /**
   * DELETE /v1/item/{itemId} : Delete an item by ID
   *
   * @param itemId The ID of the item to retrieve. (required)
   * @return Item deleted successfully. (status code 204) or Item not found (status code 404) or Internal Server Error
   * (status code 500)
   */
  @Override
  public ResponseEntity<Void> deleteItem(UUID itemId) {
    log.info("deleteItem request arrived for: {}", itemId);
    itemService.deleteItem(itemId);

    log.info("Item with ID {} deleted.", itemId);
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /v1/items : Get all items
   *
   * @param categoryId Filter items by category ID. (optional)
   * @param storageUnitId Filter items by storage unit ID. (optional)
   * @param compartmentId Filter items by compartment ID. (optional)
   * @param quantity Filter items by quantity. (optional)
   * @param isExpired Filter items by expiration status. (optional)
   * @param sortBy Sort items by a specific field. (optional)
   * @param sortOrder Sort order for items. (optional, default to asc)
   * @param name Filter items by name. (optional)
   * @param page Page number of the result set (0-indexed). (optional, default to 0)
   * @param size Number of items to return per page. (optional, default to 10)
   * @return List of items. (status code 200) or Invalid query parameters (status code 400) or Internal Server Error
   * (status code 500)
   */
  @Override
  public ResponseEntity<List<ItemDto>> getAllItems(UUID categoryId, UUID storageUnitId, UUID compartmentId, Float quantity, Boolean isExpired, String sortBy, String sortOrder, String name, Integer page, Integer size) {
    log.info("getAllItems request received with parameters - categoryId: [{}], storageUnitId: [{}], compartmentId: [{}], quantity: [{}], isExpired: [{}], name: [{}], sortBy: [{}], sortOrder: [{}], page: [{}], size: [{}]",
        categoryId, storageUnitId, compartmentId, quantity, isExpired, name, sortBy, sortOrder, page, size);

    Page<ItemDto> itemPage = itemService.getAllItems(categoryId, storageUnitId, compartmentId, quantity, isExpired, sortBy, sortOrder, name, page, size);

    List<ItemDto> itemsOnPage = itemPage.getContent();

    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("X-Total-Count", String.valueOf(itemPage.getTotalElements())); // Total items across all pages
    responseHeaders.add("X-Total-Pages", String.valueOf(itemPage.getTotalPages()));   // Total number of pages
    responseHeaders.add("X-Current-Page", String.valueOf(itemPage.getNumber()));      // Current page number (0-indexed)
    responseHeaders.add("X-Page-Size", String.valueOf(itemPage.getSize()));

    log.debug("Returning {} items for page {} (size {}). Total items: {}, Total pages: {}",
        itemsOnPage.size(), itemPage.getNumber(), itemPage.getSize(), itemPage.getTotalElements(), itemPage.getTotalPages());

    return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(itemsOnPage);

  }

  /**
   * GET /v1/item/{itemId} : Get an item by ID
   *
   * @param itemId The ID of the item to retrieve. (required)
   * @return Item found. (status code 200) or Item not found (status code 404) or Internal Server Error (status code
   * 500)
   */
  @Override
  public ResponseEntity<ItemDto> getItemById(UUID itemId) {
    log.info("getItemById request arrived with ID: {}", itemId);
    return ResponseEntity.ok(itemService.getItemById(itemId));
  }

  /**
   * PATCH /v1/item/{itemId} : Partially update an item by ID
   *
   * @param itemId The ID of the item to retrieve. (required)
   * @param itemPatchDto (required)
   * @return Item updated successfully. (status code 200) or Invalid input (status code 400) or Item not found (status
   * code 404) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<ItemDto> patchItem(UUID itemId, ItemPatchDto itemPatchDto) {
    log.info("patchItem request arrived for: {}", itemId);
    return ResponseEntity.ok(itemService.patchItem(itemId, itemPatchDto));
  }

}
