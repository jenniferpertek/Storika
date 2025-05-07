package at.pertek.storika.inventory_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.pertek.inventoryservice.commons.exception.EntryNotFoundException;
import at.pertek.inventoryservice.commons.exception.ErrorCode;
import at.pertek.inventoryservice.inventory.dto.CategoryDto;
import at.pertek.inventoryservice.inventory.entity.Category;
import at.pertek.inventoryservice.inventory.mapper.CategoryMapper;
import at.pertek.inventoryservice.inventory.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @InjectMocks
  private CategoryService categoryService;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryMapper categoryMapper;

  @Mock
  private CategoryDto categoryDto;

  @Mock
  private Category category;

  @Test
  void testGetAllCategories() {
    List<Category> mockCategories = List.of(category, category);

    when(categoryRepository.findAll()).thenReturn(mockCategories);
    when(categoryMapper.entityToDto(any())).thenReturn(categoryDto);

    List<CategoryDto> result = categoryService.getAllCategories();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(categoryRepository, times(1)).findAll();
    verify(categoryMapper, times(2)).entityToDto(any());
  }

  @Test
  void testGetCategoryById_CategoryFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(categoryMapper.entityToDto(any())).thenReturn(categoryDto);

    CategoryDto result = categoryService.getCategoryById(1L);

    assertNotNull(result);
    assertEquals(categoryDto, result);
    verify(categoryRepository, times(1)).findById(1L);
    verify(categoryMapper, times(1)).entityToDto(any());
  }

  @Test
  void testGetCategoryById_CategoryNotFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () -> {
      categoryService.getCategoryById(1L);
    });

    assertEquals(ErrorCode.INVALID_ID, exception.getErrorCode());
    verify(categoryRepository, times(1)).findById(1L);
  }

}
