package at.pertek.storika.inventory_service.mappers;

import at.pertek.storagesystem.backend.inventory.dto.ItemDto;
import at.pertek.storagesystem.backend.inventory.entity.Category;
import at.pertek.storagesystem.backend.inventory.entity.Compartment;
import at.pertek.storagesystem.backend.inventory.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ItemMapper {

  @Mapping(target = "compartment", expression = "java(mapCompartmentDto(itemDto.getCompartmentId()))")
  @Mapping(target = "category", expression = "java(mapCategoryDto(itemDto.getCategoryId()))")
  Item dtoToEntity(ItemDto itemDto);

  @Mapping(target = "compartmentId", expression = "java(mapCompartmentId(item))")
  @Mapping(target = "categoryId", expression = "java(mapCategoryId(item))")
  ItemDto entityToDto(Item item);

  @Mapping(target = "compartment", expression = "java(mapCompartmentDto(itemDto.getCompartmentId()))")
  @Mapping(target = "category", expression = "java(mapCategoryDto(itemDto.getCategoryId()))")
  @Mapping(target = "quantity", expression = "java(mapQuantity(itemDto.getQuantity()))")
  void updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);

  default Integer mapQuantity(Integer quantity) {
    return (quantity != null) ? quantity : 0;
  }

  default Long mapCategoryId(Item item) {
    return item.getCategory() != null ? item.getCategory().getId() : null;
  }

  default Category mapCategoryDto(Long categoryId) {
    if (categoryId == null) {
      return null;
    }
    Category category = new Category();
    category.setId(categoryId);
    return category;
  }

  default Long mapCompartmentId(Item item) {
    return item.getCompartment() != null ? item.getCompartment().getId() : null;
  }

  default Compartment mapCompartmentDto(Long compartmentId) {
    if (compartmentId == null) {
      return null;
    }
    Compartment compartment = new Compartment();
    compartment.setId(compartmentId);
    return compartment;
  }

}
