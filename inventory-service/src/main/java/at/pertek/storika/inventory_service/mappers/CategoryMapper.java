package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.CategoryDto;
import at.pertek.storika.inventory_service.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", imports = Category.class)
public interface CategoryMapper {

  Category dtoToEntity(CategoryDto categoryDto);

  CategoryDto entityToDto(Category category);

  void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
