package at.pertek.storika.inventory_service.services;

import at.pertek.storagesystem.backend.commons.exception.EntryNotFoundException;
import at.pertek.storagesystem.backend.commons.exception.ErrorCode;
import at.pertek.inventoryservice.dto.CategoryDto;
import at.pertek.inventoryservice.entity.Category;
import at.pertek.inventoryservice.mapper.CategoryMapper;
import at.pertek.inventoryservice.repository.CategoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public List<CategoryDto> getAllCategories() {
    return categoryRepository
        .findAll()
        .stream()
        .map(categoryMapper::entityToDto)
        .toList();
  }

  public Category getCategoryEntityById(Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new EntryNotFoundException(ErrorCode.INVALID_ID));
  }

  @Transactional
  public CategoryDto getCategoryById(Long categoryId) {
    return categoryMapper
        .entityToDto(
            getCategoryEntityById(categoryId)
        );
  }

  public CategoryDto createCategory(CategoryDto categoryDto) {
    Category category = categoryMapper.dtoToEntity(categoryDto);
    Category savedCategory = categoryRepository.save(category);

    return categoryMapper.entityToDto(savedCategory);
  }

  public CategoryDto updateCategory(long categoryId, CategoryDto categoryDto) {
    Category existingCategory = getCategoryEntityById(categoryId);

    categoryMapper.updateCategoryFromDto(categoryDto, existingCategory);

    Category updatedCategory = categoryRepository.save(existingCategory);
    return categoryMapper.entityToDto(updatedCategory);
  }

  public void deleteCategory(Long categoryId) {
    Category category = getCategoryEntityById(categoryId);
    categoryRepository.delete(category);
  }
  
}
