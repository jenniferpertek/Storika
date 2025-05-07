package at.pertek.storika.inventory_service.commons.exception;

public class EntryNotFoundException extends CustomException {

  public EntryNotFoundException(final ErrorCode errorCode) {
    super(errorCode, errorCode.getDefaultMessage());
  }

}
