package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.CategoryDto;
import at.pertek.storika.inventory_service.dto.CategoryPatchDto;
import at.pertek.storika.inventory_service.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CommonMapperUtils.class})
public interface CategoryMapper {

  Category dtoToEntity(CategoryDto categoryDto);

  @Mapping(source = "categoryId", target = "id")
  CategoryDto entityToDto(Category category);

  void patchCategoryFromDto(CategoryPatchDto categoryPatchDto, @MappingTarget Category category);

}
