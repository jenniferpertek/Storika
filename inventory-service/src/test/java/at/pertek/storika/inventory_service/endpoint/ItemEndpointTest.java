package at.pertek.storika.inventory_service.endpoints;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.pertek.inventoryservice.commons.exception.EntryNotFoundException;
import at.pertek.inventoryservice.commons.exception.ErrorCode;
import at.pertek.inventoryservice.commons.exception.RestExceptionHandler;
import at.pertek.inventoryservice.inventory.dto.ItemDto;
import at.pertek.inventoryservice.inventory.service.ItemService;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemEndpoint.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
public class ItemEndpointTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ItemService itemService;

  @Test
  void testGetAllItems() throws Exception {
    List<ItemDto> mockItems = List.of(mock(ItemDto.class), mock(ItemDto.class));
    when(itemService.getAllItems()).thenReturn(mockItems);


    mockMvc.perform(get("/v1/items"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andReturn();

  }

  @Test
  void testGetItemById_ItemFound() throws Exception {
    ItemDto dto = ItemDto.builder()
        .id(1L)
        .name("Milk")
        .quantity(2)
        .build();

    when(itemService.getItemById(1L)).thenReturn(dto);

    mockMvc.perform(get("/v1/item/{itemId}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Milk"))
        .andExpect(jsonPath("$.quantity").value(2))
        .andReturn();
  }

  @Test
  void testGetItemById_ItemNotFound() throws Exception {
    when(itemService.getItemById(111L)).thenThrow(new EntryNotFoundException(ErrorCode.INVALID_ID));

    mockMvc.perform(get("/v1/item/{itemId}", 111L))
        .andExpect(status().isNotFound())
        .andReturn();
  }

}
