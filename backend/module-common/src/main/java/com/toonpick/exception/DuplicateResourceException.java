package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * DuplicateResourceException (409)
 *  - 중복된 자원 생성 시도 시 발생합니다.
 *  - 예시: 이미 존재하는 이메일로 회원가입 시도.
 */
public class DuplicateResourceException extends CustomRuntimeException{
    public DuplicateResourceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DuplicateResourceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateResourceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateResourceException(String message) {
        super(message);
    }
}
