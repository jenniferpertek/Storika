package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.CompartmentDto;
import at.pertek.storika.inventory_service.dto.CompartmentPatchDto;
import at.pertek.storika.inventory_service.entities.Compartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CommonMapperUtils.class})
public interface CompartmentMapper {

  Compartment dtoToEntity(CompartmentDto compartmentDto);

  @Mapping(source = "compartmentId", target = "id")
  @Mapping(source = "storageUnit.storageUnitId", target = "storageUnitId")
  CompartmentDto entityToDto(Compartment compartment);

  void patchCompartmentFromDto(CompartmentPatchDto compartmentPatchDto, @MappingTarget Compartment compartment);

}
