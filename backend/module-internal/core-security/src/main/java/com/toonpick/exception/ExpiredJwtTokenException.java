package com.toonpick.exception;

import com.toonpick.common.type.ErrorCode;

/**
 * 만료되어 유효하지 않는 JWT Token 예외
 */
public class ExpiredJwtTokenException extends JwtException {

    public ExpiredJwtTokenException(String message) {
        super(message);
    }

    public ExpiredJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ExpiredJwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredJwtTokenException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ExpiredJwtTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
