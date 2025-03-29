package toonpick.exception;

import toonpick.type.ErrorCode;

/**
 * 이미 존재하는 데이터
 */
public class ResourceAlreadyExistsException extends IllegalArgumentException{
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public ResourceAlreadyExistsException(String message, String resource) {
        super(String.format("%s : %s", message, resource));
    }

    public ResourceAlreadyExistsException(ErrorCode errorCode, String resource) {
        super(String.format("%s : %s", errorCode.getMessage(), resource));
    }

    public ResourceAlreadyExistsException(ErrorCode errorCode, Long id) {
        super(String.format("%s : %s", errorCode.getMessage(), id.toString()));
    }
}
