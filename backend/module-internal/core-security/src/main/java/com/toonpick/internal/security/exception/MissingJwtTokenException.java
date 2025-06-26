package com.toonpick.internal.security.exception;

import com.toonpick.common.type.ErrorCode;

/**
 * 요청에서 Jwt 토큰을 찾을 수 없는 경우 예외
 */
public class MissingJwtTokenException extends JwtException {

    public MissingJwtTokenException(String message) {
        super(message);
    }

    public MissingJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MissingJwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingJwtTokenException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public MissingJwtTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
