package at.pertek.storika.inventory_service.commons.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module; // Good for Optional, etc.
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // For Java Time types
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openapitools.jackson.nullable.JsonNullableModule; // Import this
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.registerModule(new JsonNullableModule());

    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return mapper;
  }
}
