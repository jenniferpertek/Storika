package at.pertek.storika.inventory_service.services;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.ItemDto;
import at.pertek.storika.inventory_service.entities.Item;
import at.pertek.storika.inventory_service.mappers.ItemMapper;
import at.pertek.storika.inventory_service.repositories.ItemRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final CategoryService categoryService;
  private final CompartmentService compartmentService;
  private final ItemMapper itemMapper;

  public List<ItemDto> getAllItems() {
    return itemRepository
        .findAll()
        .stream()
        .map(itemMapper::entityToDto)
        .toList();
  }

  public Item getItemEntityById(Long itemId) {
    return itemRepository.findById(itemId)
        .orElseThrow(() -> new EntryNotFoundException(ErrorCode.INVALID_ID));
  }

  @Transactional
  public ItemDto getItemById(Long itemId) {
    return itemMapper
        .entityToDto(
            getItemEntityById(itemId)
        );
  }

  public ItemDto createItem(ItemDto itemDto) {
    Item item = itemMapper.dtoToEntity(itemDto);
    Item savedItem = itemRepository.save(item);

    return itemMapper.entityToDto(savedItem);
  }

  public ItemDto updateItem(long itemId, ItemDto itemDto) {
    Item existingItem = getItemEntityById(itemId);

    itemMapper.updateItemFromDto(itemDto, existingItem);

    Item updatedItem = itemRepository.save(existingItem);
    return itemMapper.entityToDto(updatedItem);
  }

  public void deleteItem(Long itemId) {
    Item item = getItemEntityById(itemId);
    itemRepository.delete(item);
  }

}
