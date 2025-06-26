package com.toonpick.common.exception;

import com.toonpick.common.type.ErrorCode;

/**
 * InternalServerException (500)**
 *  - 시스템 에러를 나타냅니다.
 *  - 예시: 데이터베이스 장애, 외부 API 호출 실패 등.
 */
public class InternalServerException extends CustomRuntimeException{
    public InternalServerException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InternalServerException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalServerException(String message) {
        super(message);
    }
}
