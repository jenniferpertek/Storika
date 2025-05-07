package at.pertek.storika.inventory_service.commons.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private static final String[] EXCLUDED_PATTERNS = {
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v1/**"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((requests) -> requests
        .requestMatchers(EXCLUDED_PATTERNS).permitAll()
        .anyRequest().authenticated()
    ).csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

}
