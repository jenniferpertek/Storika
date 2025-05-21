package at.pertek.storika.inventory_service.endpoints;

import at.pertek.storika.inventory_service.dto.CategoryDto;
import at.pertek.storika.inventory_service.dto.CategoryPatchDto;
import at.pertek.storika.inventory_service.endpoint.CategoryApi;
import at.pertek.storika.inventory_service.services.CategoryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryEndpoint implements CategoryApi {

  private final CategoryService categoryService;

  /**
   * POST /v1/categories : Create a new category.
   *
   * @param categoryDto (required)
   * @return Category created successfully. (status code 201) or Invalid Input (status code 400) or Internal Server
   * Error (status code 500)
   */
  @Override
  public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto) {
    log.info("createCategory request arrived for: {}", categoryDto.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
  }

  /**
   * DELETE /v1/category/{categoryId} : Delete a category by ID
   *
   * @param categoryId The ID of the category to retrieve. (required)
   * @return Category deleted successfully. (status code 204) or Category not found (status code 404) or Conflict -
   * Category cannot be deleted as it is currently associated with items. (status code 409) or Internal Server Error
   * (status code 500)
   */
  @Override
  public ResponseEntity<Void> deleteCategory(UUID categoryId) {
    log.info("deleteCategory request arrived for: {}", categoryId);
    categoryService.deleteCategory(categoryId);

    log.info("Category with ID {} deleted.", categoryId);
    return ResponseEntity.noContent().build();
  }

  /**
   * GET /v1/categories : Get all categories
   *
   * @param name Filter by category name (optional)
   * @param sortBy Field to sort the categories by. Allowed values typically include &#39;name&#39;. (optional, default
   * to name)
   * @param sortOrder Sort order. (optional, default to asc)
   * @param page Page number of the result set (0-indexed). (optional, default to 0)
   * @param size Number of items to return per page. (optional, default to 10)
   * @return A List of categories. (status code 200) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<List<CategoryDto>> getAllCategories(String name, String sortBy, String sortOrder, Integer page, Integer size) {
    log.info("getAllCategories request arrived.");
    return ResponseEntity.ok(categoryService.getAllCategories(name, sortBy, sortOrder, page, size));
  }

  /**
   * GET /v1/category/{categoryId} : Get a category by ID
   *
   * @param categoryId The ID of the category to retrieve. (required)
   * @return Category found. (status code 200) or Category not found (status code 404) or Internal Server Error (status
   * code 500)
   */
  @Override
  public ResponseEntity<CategoryDto> getCategoryById(UUID categoryId) {
    log.info("getCategoryById request arrived with ID: {}", categoryId);
    return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
  }

  /**
   * PATCH /v1/category/{categoryId} : Partially update a category by ID
   *
   * @param categoryId The ID of the category to retrieve. (required)
   * @param categoryPatchDto (required)
   * @return Category updated successfully. (status code 200) or Invalid Input (status code 400) or Category not found
   * (status code 404) or Internal Server Error (status code 500)
   */
  @Override
  public ResponseEntity<CategoryDto> patchCategory(UUID categoryId, CategoryPatchDto categoryPatchDto) {
    log.info("updateCategory request arrived for: {}", categoryId);
    return ResponseEntity.ok(categoryService.patchCategory(categoryId, categoryPatchDto));
  }

}
