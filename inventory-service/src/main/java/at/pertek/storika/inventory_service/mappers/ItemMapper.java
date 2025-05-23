package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.ItemDto;
import at.pertek.storika.inventory_service.dto.ItemPatchDto;
import at.pertek.storika.inventory_service.entities.Item;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CommonMapperUtils.class})
public interface ItemMapper {

  Item dtoToEntity(ItemDto itemDto);

  ItemDto entityToDto(Item item);

  void patchItemFromDto(ItemPatchDto itemPatchDto, @MappingTarget Item item);

  // For JsonNullable<Float> (quantity in DTO) <-> int (quantity in Entity)
  default int mapJsonNullableFloatToInt(JsonNullable<Float> jsonNullableFloat) {
    if (jsonNullableFloat == null || !jsonNullableFloat.isPresent() || jsonNullableFloat.get() == null) {
      return 0;
    }
    return jsonNullableFloat.get().intValue();
  }

  default JsonNullable<Float> mapIntToJsonNullableFloat(int value) {
    return JsonNullable.of((float) value);
  }

  // For JsonNullable<LocalDate> (purchaseDate, expirationDate in DTO) <-> LocalDate (purchaseDate, expirationDate in Entity)
  default LocalDate mapJsonNullableLocalDateToLocalDate(JsonNullable<LocalDate> jsonNullableLocalDate) {
    if (jsonNullableLocalDate == null || !jsonNullableLocalDate.isPresent()) {
      return null;
    }
    return jsonNullableLocalDate.get();
  }

  default JsonNullable<LocalDate> mapLocalDateToJsonNullableLocalDate(LocalDate localDate) {
    return JsonNullable.of(localDate);
  }

 /*
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

  default Category mapCategoryDto(UUID categoryId) {
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
  }*/

}
