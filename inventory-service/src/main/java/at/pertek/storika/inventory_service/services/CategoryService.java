package at.pertek.storika.inventory_service.services;

import at.pertek.storika.inventory_service.commons.exception.EntryNotFoundException;
import at.pertek.storika.inventory_service.commons.exception.ErrorCode;
import at.pertek.storika.inventory_service.dto.CategoryDto;
import at.pertek.storika.inventory_service.dto.CategoryPatchDto;
import at.pertek.storika.inventory_service.entities.Category;
import at.pertek.storika.inventory_service.mappers.CategoryMapper;
import at.pertek.storika.inventory_service.repositories.CategoryRepository;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  private static final String DEFAULT_CATEGORY_SORT_FIELD = "name";
  private static final Set<String> ALLOWED_CATEGORY_SORT_FIELDS = Set.of(
      DEFAULT_CATEGORY_SORT_FIELD
  );

  @Transactional(readOnly = true)
  public Page<CategoryDto> getAllCategories(String name, String sortBy, String sortOrder, Integer page, Integer size) {
    log.debug("Fetching categories with filters - name: [{}], sortBy: [{}], sortOrder: [{}], page: [{}], size: [{}]",
        name, sortBy, sortOrder, page, size);

    Pageable pageable = PaginationUtil.createPageable(
        page,
        size,
        sortBy,
        sortOrder,
        DEFAULT_CATEGORY_SORT_FIELD,
        ALLOWED_CATEGORY_SORT_FIELDS
    );

    log.debug("Constructed Pageable: {}", pageable);

    Page<Category> categoryPage = categoryRepository.findByOptionalFilters(name, pageable);

    return categoryPage.map(categoryMapper::entityToDto);
  }

  @Transactional(readOnly = true)
  public Category getCategoryEntityById(UUID categoryId) {
    log.debug("Fetching category entity with id: {}", categoryId);

    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> {
          log.warn("Category not found with id: {}", categoryId);
          return new EntryNotFoundException(ErrorCode.INVALID_ID);
        });
  }

  @Transactional
  public CategoryDto getCategoryById(UUID categoryId) {
    log.debug("Fetching category DTO with id: {}", categoryId);

    return categoryMapper.entityToDto(getCategoryEntityById(categoryId));
  }

  @Transactional
  public CategoryDto createCategory(CategoryDto categoryDto) {
    log.info("Creating new category with name: {}", categoryDto.getName());

    Category category = categoryMapper.dtoToEntity(categoryDto);
    Category savedCategory = categoryRepository.save(category);

    log.info("Successfully created category with id: {}", savedCategory.getCategoryId());

    return categoryMapper.entityToDto(savedCategory);
  }

  @Transactional
  public CategoryDto patchCategory(UUID categoryId, CategoryPatchDto categoryPatchDto) {
    log.info("Updating category with id: {}", categoryId);

    Category existingCategory = getCategoryEntityById(categoryId);
    categoryMapper.patchCategoryFromDto(categoryPatchDto, existingCategory);
    Category updatedCategory = categoryRepository.save(existingCategory);

    log.info("Successfully updated category with id: {}", updatedCategory.getCategoryId());

    return categoryMapper.entityToDto(updatedCategory);
  }

  @Transactional
  public void deleteCategory(UUID categoryId) {
    log.info("Deleting category with id: {}", categoryId);

    Category category = getCategoryEntityById(categoryId);
    categoryRepository.delete(category);

    log.info("Successfully deleted category with id: {}", categoryId);
  }

}
