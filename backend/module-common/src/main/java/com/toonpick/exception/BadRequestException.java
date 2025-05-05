package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * BadRequestException (400)
 *  - 잘못된 입력값에 대한 예외입니다.
 *  - 예시: 유효성 검사 실패, 파라미터 오류 등.
 */
public class BadRequestException extends CustomRuntimeException{
    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public BadRequestException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
