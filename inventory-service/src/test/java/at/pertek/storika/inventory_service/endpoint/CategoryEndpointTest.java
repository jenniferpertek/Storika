package at.pertek.storika.inventory_service.endpoints;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.pertek.inventoryservice.commons.exception.EntryNotFoundException;
import at.pertek.inventoryservice.commons.exception.ErrorCode;
import at.pertek.inventoryservice.commons.exception.RestExceptionHandler;
import at.pertek.inventoryservice.inventory.dto.CategoryDto;
import at.pertek.inventoryservice.inventory.service.CategoryService;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryEndpoint.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RestExceptionHandler.class)
public class CategoryEndpointTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CategoryService categoryService;

  @Test
  void testGetAllCategories() throws Exception {
    List<CategoryDto> mockCategories = List.of(mock(CategoryDto.class), mock(CategoryDto.class));
    when(categoryService.getAllCategories()).thenReturn(mockCategories);


    mockMvc.perform(get("/v1/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andReturn();

  }

  @Test
  void testGetCategoryById_CategoryFound() throws Exception {
    CategoryDto dto = CategoryDto.builder()
        .id(1L)
        .name("Food")
        .build();

    when(categoryService.getCategoryById(1L)).thenReturn(dto);

    mockMvc.perform(get("/v1/category/{categoryId}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Food"))
        .andReturn();
  }

  @Test
  void testGetCategoryById_CategoryNotFound() throws Exception {
    when(categoryService.getCategoryById(111L)).thenThrow(new EntryNotFoundException(ErrorCode.INVALID_ID));

    mockMvc.perform(get("/v1/category/{categoryId}", 111L))
        .andExpect(status().isNotFound())
        .andReturn();
  }


}
