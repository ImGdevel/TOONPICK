package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * AccessDeniedException (403)
 *  - 인증은 되었으나, 권한이 부족할 경우 발생하는 예외입니다.
 *  - 예시: 권한이 필요한 자원에 접근 시도.
 */
public class AccessDeniedException extends CustomRuntimeException{
    public AccessDeniedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AccessDeniedException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
