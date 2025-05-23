package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.CategoryDto;
import at.pertek.storika.inventory_service.dto.ItemDto;
import at.pertek.storika.inventory_service.dto.ItemPatchDto;
import at.pertek.storika.inventory_service.entities.Category;
import at.pertek.storika.inventory_service.entities.Item;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CommonMapperUtils.class})
public interface ItemMapper {

  Item dtoToEntity(ItemDto itemDto);

  @Mapping(source = "itemId", target = "id")
  @Mapping(source = "storageUnit.storageUnitId", target = "storageUnitId")
  @Mapping(target = "categoryId", expression = "java(CommonMapperUtils.categoryToUuidJsonNullable(item.getCategory()))")
  @Mapping(target = "compartmentId", expression = "java(CommonMapperUtils.compartmentToUuidJsonNullable(item.getCompartment()))")
  ItemDto entityToDto(Item item);

  void patchItemFromDto(ItemPatchDto itemPatchDto, @MappingTarget Item item);

}
