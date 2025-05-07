package at.pertek.storika.inventory_service.commons.exception;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

  @NotEmpty
  private String message;

  @NotEmpty
  private String errorCode;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  @NotEmpty
  private String path;

  @NotEmpty
  private int httpStatus;


  public static ErrorResponse of(String errorCode, String message, int httpStatus, String path) {
    return builder()
        .errorCode(errorCode)
        .message(message)
        .httpStatus(httpStatus)
        .path(path)
        .build();
  }
}
