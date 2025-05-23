package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.StorageUnitDto;
import at.pertek.storika.inventory_service.dto.StorageUnitPatchDto;
import at.pertek.storika.inventory_service.entities.StorageUnit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {CommonMapperUtils.class})
public interface StorageUnitMapper {

  StorageUnit dtoToEntity(StorageUnitDto storageUnitDto);

  StorageUnitDto entityToDto(StorageUnit storageUnit);

  void patchStorageUnitFromDto(StorageUnitPatchDto storageUnitPatchDto, @MappingTarget StorageUnit storageUnit);

}
