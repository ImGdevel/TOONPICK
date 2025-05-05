package com.toonpick.exception.handler;

import com.toonpick.dto.ErrorResponse;
import com.toonpick.exception.AccessDeniedException;
import com.toonpick.exception.AuthenticationException;
import com.toonpick.exception.BadRequestException;
import com.toonpick.exception.BusinessException;
import com.toonpick.exception.DuplicateResourceException;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.exception.InternalServerException;

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
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getErrorCode()));
    }

    /**
     * AuthenticationException(401 Unauthorized):  로그인 실패, 인증 실패
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getErrorCode()));
    }

    /**
     * AccessDeniedException(403 Forbidden): 인증은 됐지만, 권한이 부족함
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getErrorCode()));
    }

    /**
     * EntityNotFoundException(404 Not Found): 요청한 데이터를 찾을 수 없음
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getErrorCode()));
    }

    /**
     * DuplicateResourceException(409 Conflict): 동일 데이터 중복 생성 시도
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getErrorCode()));
    }


    /**
     * BusinessException(422 Unprocessable Entity): 도메인/비즈니스 규칙 위반
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getErrorCode()));
    }

    /**
     * InternalServerException(500 Internal Server Error): 예기치 못한 서버 시스템 에러
     */
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getErrorCode()));
    }

}
