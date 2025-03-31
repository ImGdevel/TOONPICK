package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * 요청에서 Jwt 토큰을 찾을 수 없는 경우 예외
 */
public class MissingJwtTokenException extends JwtException {
    public MissingJwtTokenException(String message) {
        super(message);
    }

    public MissingJwtTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public MissingJwtTokenException(String message, String resource) {
        super(String.format("%s : %s", message, resource));
    }

    public MissingJwtTokenException(ErrorCode errorCode, String resource) {
        super(String.format("%s : %s", errorCode.getMessage(), resource));
    }

    public MissingJwtTokenException(ErrorCode errorCode, Long id) {
        super(String.format("%s : %s", errorCode.getMessage(), id.toString()));
    }
}
