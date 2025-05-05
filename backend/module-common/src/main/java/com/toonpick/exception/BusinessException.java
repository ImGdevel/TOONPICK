package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * BusinessException (422)
 *  - 도메인 규칙 위반 시 발생하는 예외입니다.
 *  - 예시: 비즈니스 로직에 의한 제한 사항 위반.
 */
public class BusinessException extends CustomRuntimeException{
    public BusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(String message) {
        super(message);
    }
}
