package com.toonpick.common.exception;

import com.toonpick.common.type.ErrorCode;

/**
 * AuthenticationException (401)
 *  - 인증 실패 시 발생하는 예외입니다.
 *  - 예시: 로그인 실패, 토큰 없음/만료 등.
 */
public class AuthenticationException extends CustomRuntimeException{
    public AuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthenticationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
