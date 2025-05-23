package at.pertek.storika.inventory_service.services;
/*
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.ItemDto;
import at.pertek.storika.inventory_service.entities.Item;
import at.pertek.storika.inventory_service.mappers.ItemMapper;
import at.pertek.storika.inventory_service.repositories.ItemRepository;
import at.pertek.storika.inventory_service.services.ItemService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

  @InjectMocks
  private ItemService itemService;

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private ItemMapper itemMapper;

  @Mock
  private ItemDto itemDto;

  @Mock
  private Item item;

  @Test
  void testGetAllItems() {
    List<Item> mockItems = List.of(item, item);

    when(itemRepository.findAll()).thenReturn(mockItems);
    when(itemMapper.entityToDto(any())).thenReturn(itemDto);

    List<ItemDto> result = itemService.getAllItems();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(itemRepository, times(1)).findAll();
    verify(itemMapper, times(2)).entityToDto(any());
  }

  @Test
  void testGetItemById_ItemFound() {
    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    when(itemMapper.entityToDto(any())).thenReturn(itemDto);

    ItemDto result = itemService.getItemById(1L);

    assertNotNull(result);
    assertEquals(itemDto, result);
    verify(itemRepository, times(1)).findById(1L);
    verify(itemMapper, times(1)).entityToDto(any());
  }

  @Test
  void testGetItemById_ItemNotFound() {
    when(itemRepository.findById(1L)).thenReturn(Optional.empty());

    EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () -> {
      itemService.getItemById(1L);
    });

    assertEquals(ErrorCode.INVALID_ID, exception.getErrorCode());
    verify(itemRepository, times(1)).findById(1L);
  }

}*/
