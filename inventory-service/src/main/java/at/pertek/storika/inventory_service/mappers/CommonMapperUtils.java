package at.pertek.storika.inventory_service.mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Component
public class CommonMapperUtils {

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

}
