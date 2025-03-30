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
        super(errorCode.getMessage());
    }

    public InvalidJwtTokenException(String message, String resource) {
        super(String.format("%s : %s", message, resource));
    }

    public InvalidJwtTokenException(ErrorCode errorCode, String resource) {
        super(String.format("%s : %s", errorCode.getMessage(), resource));
    }

    public InvalidJwtTokenException(ErrorCode errorCode, Long id) {
        super(String.format("%s : %s", errorCode.getMessage(), id.toString()));
    }
}
