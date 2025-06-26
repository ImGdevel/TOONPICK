package com.toonpick.exception;

import com.toonpick.common.exception.AuthenticationException;
import com.toonpick.common.type.ErrorCode;

/**
 * JWT 토큰 예외
 */
public class JwtException extends AuthenticationException {

    public JwtException(String message) {
        super(message);
    }

    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public JwtException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
