package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.LocationDto;
import at.pertek.storika.inventory_service.dto.LocationPatchDto;
import at.pertek.storika.inventory_service.entities.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CommonMapperUtils.class})
public interface LocationMapper {

  Location dtoToEntity(LocationDto locationDto);

  @Mapping(source = "locationId", target = "id")
  LocationDto entityToDto(Location location);

  void patchLocationFromDto(LocationPatchDto locationPatchDto, @MappingTarget Location location);

}
