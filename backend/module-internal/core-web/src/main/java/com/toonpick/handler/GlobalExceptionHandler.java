package com.toonpick.handler;

import com.toonpick.common.exception.AccessDeniedException;
import com.toonpick.common.exception.AuthenticationException;
import com.toonpick.common.exception.BadRequestException;
import com.toonpick.common.exception.BusinessException;
import com.toonpick.common.exception.DuplicateResourceException;
import com.toonpick.common.exception.EntityNotFoundException;
import com.toonpick.common.exception.InternalServerException;

import com.toonpick.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * BadRequestException(400 Bad Request): 잘못된 입력
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ex.getErrorCode()));
    }

    /**
     * AuthenticationException(401 Unauthorized):  로그인 실패, 인증 실패
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(ex.getErrorCode()));
    }

    /**
     * AccessDeniedException(403 Forbidden): 인증은 됐지만, 권한이 부족함
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail(ex.getErrorCode()));
    }

    /**
     * EntityNotFoundException(404 Not Found): 요청한 데이터를 찾을 수 없음
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(ex.getErrorCode()));
    }

    /**
     * DuplicateResourceException(409 Conflict): 동일 데이터 중복 생성 시도
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(ex.getErrorCode()));
    }


    /**
     * BusinessException(422 Unprocessable Entity): 도메인/비즈니스 규칙 위반
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.fail(ex.getErrorCode()));
    }

    /**
     * InternalServerException(500 Internal Server Error): 예기치 못한 서버 시스템 에러
     */
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiResponse> handleInternalServerException(InternalServerException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(ex.getErrorCode()));
    }

    /**
     * Exception(500 Internal Server Error): 알 수 없는 시스템 에러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(ex.getMessage()));
    }

}
