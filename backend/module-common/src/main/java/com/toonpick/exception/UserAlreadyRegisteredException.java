package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * 이미 가입된 유저 예외
 */
public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

    public UserAlreadyRegisteredException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public UserAlreadyRegisteredException(ErrorCode errorCode, String resource) {
        super(String.format("%s : %s", errorCode.getMessage(), resource));
    }

}
