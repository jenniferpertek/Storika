package at.pertek.storika.inventory_service.services;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.ItemDto;
import at.pertek.storika.inventory_service.dto.ItemPatchDto;
import at.pertek.storika.inventory_service.entities.Category;
import at.pertek.storika.inventory_service.entities.Compartment;
import at.pertek.storika.inventory_service.entities.Item;
import at.pertek.storika.inventory_service.entities.StorageUnit;
import at.pertek.storika.inventory_service.mappers.ItemMapper;
import at.pertek.storika.inventory_service.repositories.ItemRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@AllArgsConstructor
@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final CategoryService categoryService;
  private final CompartmentService compartmentService;
  private final StorageUnitService storageUnitService;
  private final ItemMapper itemMapper;

  @Transactional(readOnly = true)
  public Page<ItemDto> getAllItems( UUID categoryId, UUID storageUnitId, UUID compartmentId,
                                    Float quantity, Boolean isExpired,
                                    String sortBy, String sortOrder, String name,
                                    Integer page, Integer size) {
    log.debug("Fetching items with filters - categoryId: [{}], storageUnitId: [{}], compartmentId: [{}], " +
            "quantity: [{}], isExpired: [{}], name: [{}], sortBy: [{}], sortOrder: [{}], page: [{}], size: [{}]",
        categoryId, storageUnitId, compartmentId, quantity, isExpired, name, sortBy, sortOrder, page, size);

    Sort sort = Sort.unsorted();
    if (StringUtils.hasText(sortBy)) {
      Sort.Direction direction = StringUtils.hasText(sortOrder) && "desc".equalsIgnoreCase(sortOrder) ?
          Sort.Direction.DESC : Sort.Direction.ASC;
      sort = Sort.by(direction, sortBy);
    }

    int pageNumber = (page != null && page >= 0) ? page : 0;
    int pageSize = (size != null && size > 0) ? size : 10;

    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

    Page<Item> itemPage = itemRepository.findByOptionalFilters(
        categoryId, storageUnitId, compartmentId,
        quantity, isExpired, name,
        pageable
    );

    return itemPage.map(itemMapper::entityToDto);
  }

  @Transactional(readOnly = true)
  public Item getItemEntityById(UUID itemId) {
    log.debug("Fetching item entity with id: {}", itemId);

    return itemRepository
        .findById(itemId)
        .orElseThrow(() -> {
          log.warn("Item not found with id: {}", itemId);
          return new EntryNotFoundException(ErrorCode.INVALID_ID);
        });
  }

  @Transactional(readOnly = true)
  public ItemDto getItemById(UUID itemId) {
    log.debug("Fetching item DTO with id: {}", itemId);

    return itemMapper.entityToDto(getItemEntityById(itemId));
  }

  @Transactional
  public ItemDto createItem(ItemDto itemDto) {
    log.info("Creating new item with name: {}", itemDto.getName());

    if (itemDto.getStorageUnitId() == null) {
      log.error("StorageUnitId is required to create an item.");
      throw new IllegalArgumentException("StorageUnitId cannot be null when creating an item.");
    }

    StorageUnit storageUnit = storageUnitService.getStorageUnitEntityById(itemDto.getStorageUnitId());

    Category category = null;
    UUID categoryUuid = itemDto.getCategoryId() != null ? itemDto.getCategoryId().orElse(null) : null;
    if (categoryUuid != null) {
      category = categoryService.getCategoryEntityById(categoryUuid);
    }

    Compartment compartment = null;
    UUID compartmentUuid = itemDto.getCompartmentId() != null ? itemDto.getCompartmentId().orElse(null) : null;
    if (compartmentUuid != null) {
      compartment = compartmentService.getCompartmentEntityById(compartmentUuid);
    }

    Item item = itemMapper.dtoToEntity(itemDto);
    item.setStorageUnit(storageUnit);
    item.setCategory(category);
    item.setCompartment(compartment);

    Item savedItem = itemRepository.save(item);

    log.info("Successfully created item with id: {}", savedItem.getItemId());

    return itemMapper.entityToDto(savedItem);
  }

  @Transactional
  public ItemDto patchItem(UUID itemId, ItemPatchDto itemPatchDto) {
    log.info("Updating item with id: {}", itemId);

    Item existingItem = getItemEntityById(itemId);

    if (itemPatchDto.getStorageUnitId() != null &&
        (existingItem.getStorageUnit() == null ||
            !existingItem.getStorageUnit().getStorageUnitId().equals(itemPatchDto.getStorageUnitId()))) {
      log.debug("Updating storage unit for item id: {}", itemId);
      StorageUnit newStorageUnit = storageUnitService.getStorageUnitEntityById(itemPatchDto.getStorageUnitId());
      existingItem.setStorageUnit(newStorageUnit);
    }

    if (itemPatchDto.getCategoryId() != null && itemPatchDto.getCategoryId().isPresent()) {
      UUID categoryUuid = itemPatchDto.getCategoryId().orElse(null);
      if (categoryUuid != null) {
        Category newCategory = categoryService.getCategoryEntityById(categoryUuid);
        existingItem.setCategory(newCategory);
      } else {
        existingItem.setCategory(null);
      }
    }

    if (itemPatchDto.getCompartmentId() != null && itemPatchDto.getCompartmentId().isPresent()) {
      UUID compartmentUuid = itemPatchDto.getCompartmentId().orElse(null);
      if (compartmentUuid != null) {
        Compartment newCompartment = compartmentService.getCompartmentEntityById(compartmentUuid);
        existingItem.setCompartment(newCompartment);
      } else {
        existingItem.setCompartment(null);
      }
    }

    itemMapper.patchItemFromDto(itemPatchDto, existingItem);
    Item updatedItem = itemRepository.save(existingItem);

    log.info("Successfully updated item with id: {}", updatedItem.getItemId());

    return itemMapper.entityToDto(updatedItem);
  }

  public void deleteItem(UUID itemId) {
    log.info("Deleting item with id: {}", itemId);

    Item item = getItemEntityById(itemId);
    itemRepository.delete(item);

    log.info("Successfully deleted item with id: {}", itemId);
  }

  @Transactional(readOnly = true)
  public List<ItemDto> getItemsByStorageUnitId(UUID storageUnitId) {
    log.debug("Fetching items for storage unit id: {}", storageUnitId);

    storageUnitService.getStorageUnitEntityById(storageUnitId);

    return itemRepository.findByStorageUnitId(storageUnitId)
        .stream()
        .map(itemMapper::entityToDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ItemDto> getItemsByCompartmentId(UUID compartmentId) {
    log.debug("Fetching items for compartment id: {}", compartmentId);

    compartmentService.getCompartmentEntityById(compartmentId);

    return itemRepository.findByCompartmentId(compartmentId)
        .stream()
        .map(itemMapper::entityToDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ItemDto> getItemsByCategoryId(UUID categoryId) {
    log.debug("Fetching items for category id: {}", categoryId);

    categoryService.getCategoryEntityById(categoryId);

    return itemRepository.findByCategoryId(categoryId)
        .stream()
        .map(itemMapper::entityToDto)
        .toList();
  }

}
