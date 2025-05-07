package at.pertek.storika.inventory_service.commons.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Map<ErrorCode, HttpStatus> errorCodeToHttpStatusMap = Map.of(
      ErrorCode.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE,
      ErrorCode.GENERAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
      ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST,
      ErrorCode.INVALID_ID, HttpStatus.NOT_FOUND
  );

  private HttpStatus mapErrorCodeToHttpStatus(ErrorCode code) {
    return errorCodeToHttpStatusMap.getOrDefault(code, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException customException,
                                                             HttpServletRequest request) {
    log.error("An error occurred: {}", customException.getMessage(), customException);
    String path = request.getRequestURI();
    String errorCode = customException.getErrorCode().getCode();
    HttpStatus httpStatus = mapErrorCodeToHttpStatus(customException.getErrorCode());

    ErrorResponse errorResponse = ErrorResponse.of(errorCode, customException.getMessage(), httpStatus.value(), path);

    return new ResponseEntity<>(errorResponse, httpStatus);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorResponse> defaultErrorHandler(Exception exception, HttpServletRequest request) {
    log.error("An error occurred: {}", exception.getMessage(), exception);
    String path = request.getRequestURI();
    String message = ErrorCode.GENERAL_ERROR.getDefaultMessage();
    String errorCode = ErrorCode.GENERAL_ERROR.getCode();

    ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, HttpStatus.INTERNAL_SERVER_ERROR.value(), path);

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
