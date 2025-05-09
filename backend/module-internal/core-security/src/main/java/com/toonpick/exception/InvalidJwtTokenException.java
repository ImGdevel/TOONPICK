package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * 유효하지 않는 형식의 JWT 토큰 형식 예외
 */
public class InvalidJwtTokenException extends JwtException {

    public InvalidJwtTokenException(String message) {
        super(message);
    }

    public InvalidJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidJwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtTokenException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public InvalidJwtTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
