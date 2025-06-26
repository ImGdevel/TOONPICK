package com.toonpick.internal.security.exception;

import com.toonpick.common.type.ErrorCode;

/**
 * 저장된 리프레시 토큰을 찾을 수 없을 경우 예외
 */
public class RefreshTokenNotFoundException extends RuntimeException {

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

    public RefreshTokenNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public RefreshTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenNotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
