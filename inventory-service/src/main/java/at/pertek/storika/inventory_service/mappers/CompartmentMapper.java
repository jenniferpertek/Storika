package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.dto.CategoryDto;
import at.pertek.storika.inventory_service.dto.CompartmentDto;
import at.pertek.storika.inventory_service.entities.Category;
import at.pertek.storika.inventory_service.entities.Compartment;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CompartmentMapper {

  Compartment dtoToEntity(CompartmentDto compartmentDto);

  CompartmentDto entityToDto(Compartment compartment);

  void updateCompartmentFromDto(CompartmentDto compartmentDto, @MappingTarget Compartment compartment);

  // For JsonNullable<String> (description) <-> String (description)
  default String mapJsonNullableToString(JsonNullable<String> jsonNullableString) {
    if (jsonNullableString == null || !jsonNullableString.isPresent()) {
      return null;
    }
    return jsonNullableString.orElse(null);
  }

  default JsonNullable<String> mapStringToJsonNullable(String string) {
    return JsonNullable.of(string);
  }

  // For Instant (createdAt, updatedAt in DTO) <-> OffsetDateTime (createdAt, updatedAt in Entity)
  default OffsetDateTime mapInstantToOffsetDateTime(Instant instant) {
    if (instant == null) {
      return null;
    }
    return instant.atOffset(ZoneOffset.UTC);
  }

  default Instant mapOffsetDateTimeToInstant(OffsetDateTime offsetDateTime) {
    if (offsetDateTime == null) {
      return null;
    }
    return offsetDateTime.toInstant();
  }

}
