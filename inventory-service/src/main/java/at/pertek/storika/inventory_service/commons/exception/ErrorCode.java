package at.pertek.storika.inventory_service.commons.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // General Errors
  SERVICE_UNAVAILABLE("1000", "Service is unavailable"),
  GENERAL_ERROR("1001", "Internal server error"),

  // Validation errors
  INVALID_REQUEST("1100", "Invalid request"),
  INVALID_ID("1101", "Invalid ID");

  private final String code;
  private final String defaultMessage;

}
