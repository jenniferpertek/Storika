package at.pertek.storika.inventory_service.commons.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String message;
}