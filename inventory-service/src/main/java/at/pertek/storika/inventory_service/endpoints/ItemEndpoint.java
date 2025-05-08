package at.pertek.storika.inventory_service.endpoints;

import at.pertek.storika.inventory_service.dto.ItemDto;
import at.pertek.storika.inventory_service.endpoint.ItemApi;
import at.pertek.storika.inventory_service.services.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemEndpoint implements ItemApi {

  private final ItemService itemService;

  @Override
  public ResponseEntity<List<ItemDto>> getAllItems() {
    log.info("getAllItems request arrived.");
    return ResponseEntity.ok(itemService.getAllItems());
  }

  @Override
  public ResponseEntity<ItemDto> getItemById(Long itemId) {
    log.info("getItemById request arrived with ID: {}", itemId);
    return ResponseEntity.ok(itemService.getItemById(itemId));
  }

  @Override
  public ResponseEntity<ItemDto> createItem(ItemDto itemDto) {
    log.info("createItem request arrived for: {}", itemDto.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(itemDto));
  }

  @Override
  public ResponseEntity<ItemDto> updateItem(Long itemId, ItemDto itemDto) {
    log.info("updateItem request arrived for: {}", itemId);
    return ResponseEntity.ok(itemService.updateItem(itemId, itemDto));
  }

  @Override
  public ResponseEntity<Void> deleteItem(Long itemId) {
    log.info("deleteItem request arrived for: {}", itemId);
    itemService.deleteItem(itemId);
    log.info("Item with ID {} deleted.", itemId);
    return ResponseEntity.noContent().build();
  }

}
