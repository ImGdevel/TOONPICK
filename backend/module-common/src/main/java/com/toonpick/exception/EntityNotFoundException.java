package com.toonpick.exception;

import com.toonpick.type.ErrorCode;

/**
 * EntityNotFoundException (404)
 *  - 요청한 데이터를 찾을 수 없을 때 발생합니다.
 *  - 예시: 특정 회원 또는 게시글 조회 실패.
 */
public class EntityNotFoundException extends CustomRuntimeException {

    public EntityNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public EntityNotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
