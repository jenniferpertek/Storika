package at.pertek.storika.inventory_service.mappers;

import at.pertek.storika.inventory_service.entities.Category;
import at.pertek.storika.inventory_service.entities.Compartment;
import at.pertek.storika.inventory_service.entities.Location;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Component
public class CommonMapperUtils {

  public static JsonNullable<UUID> categoryToUuidJsonNullable(Category category) {
    if (category != null && category.getCategoryId() != null) {
      return JsonNullable.of(category.getCategoryId());
    }
    return JsonNullable.of(null);
  }

  public static JsonNullable<UUID> compartmentToUuidJsonNullable(Compartment compartment) {
    if (compartment != null && compartment.getCompartmentId() != null) {
      return JsonNullable.of(compartment.getCompartmentId());
    }
    return JsonNullable.of(null);
  }

  public static String mapJsonNullableToString(JsonNullable<String> jsonNullableString) {
    if (jsonNullableString == null || !jsonNullableString.isPresent()) {
      return null;
    }
    return jsonNullableString.orElse(null);
  }

  public static JsonNullable<String> mapStringToJsonNullable(String string) {
    return JsonNullable.of(string);
  }

  public static OffsetDateTime mapInstantToOffsetDateTime(Instant instant) {
    if (instant == null) {
      return null;
    }
    return instant.atOffset(ZoneOffset.UTC);
  }

  public static Instant mapOffsetDateTimeToInstant(OffsetDateTime offsetDateTime) {
    if (offsetDateTime == null) {
      return null;
    }
    return offsetDateTime.toInstant();
  }

  public static int mapJsonNullableFloatToInt(JsonNullable<Float> jsonNullableFloat) {
    if (jsonNullableFloat == null || !jsonNullableFloat.isPresent() || jsonNullableFloat.get() == null) {
      return 0;
    }
    return jsonNullableFloat.get().intValue();
  }

  public static JsonNullable<Float> mapIntToJsonNullableFloat(int value) {
    return JsonNullable.of((float) value);
  }

  public static LocalDate mapJsonNullableLocalDateToLocalDate(JsonNullable<LocalDate> jsonNullableLocalDate) {
    if (jsonNullableLocalDate == null || !jsonNullableLocalDate.isPresent()) {
      return null;
    }
    return jsonNullableLocalDate.get();
  }

  public static JsonNullable<LocalDate> mapLocalDateToJsonNullableLocalDate(LocalDate localDate) {
    return JsonNullable.of(localDate);
  }

}
