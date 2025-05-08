package at.pertek.storika.inventory_service.endpoints;

import at.pertek.storika.inventory_service.dto.CategoryDto;
import at.pertek.storika.inventory_service.endpoint.CategoryApi;
import at.pertek.storika.inventory_service.services.CategoryService;
import java.util.List;
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

  @Override
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    log.info("getAllCategories request arrived.");
    return ResponseEntity.ok(categoryService.getAllCategories());
  }

  @Override
  public ResponseEntity<CategoryDto> getCategoryById(Long categoryId) {
    log.info("getCategoryById request arrived with ID: {}", categoryId);
    return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
  }

  @Override
  public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto) {
    log.info("createCategory request arrived for: {}", categoryDto.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
  }

  @Override
  public ResponseEntity<CategoryDto> updateCategory(Long categoryId, CategoryDto categoryDto) {
    log.info("updateCategory request arrived for: {}", categoryId);
    return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
  }

  @Override
  public ResponseEntity<Void> deleteCategory(Long categoryId) {
    log.info("deleteCategory request arrived for: {}", categoryId);
    categoryService.deleteCategory(categoryId);
    log.info("Category with ID {} deleted.", categoryId);
    return ResponseEntity.noContent().build();
  }

}
