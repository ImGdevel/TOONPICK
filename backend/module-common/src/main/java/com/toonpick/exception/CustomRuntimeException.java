package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * 기본 베이스가 되는 Error입니다.
 * 모든 RuntimeException 해당 클래스를 상속 받습니다.
 */
public class CustomRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomRuntimeException(String message) {
        super(message);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }

    public CustomRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }

    public CustomRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public CustomRuntimeException(ErrorCode errorCode, String message) {
        super(String.format("%s: %s", errorCode.getMessage(), message));
        this.errorCode = errorCode;
    }

     public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.format("ErrorCode: %s, Message: %s", errorCode.name(), getMessage());
    }

}
