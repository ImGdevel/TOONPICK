package com.toonpick.worker.application.exception;

import com.toonpick.common.exception.BadRequestException;
import com.toonpick.worker.application.controller.AdminWorkerController;
import com.toonpick.worker.dto.WebtoonTriggerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = AdminWorkerController.class)
public class WorkerExceptionHandler {

    /**
     * BadRequestException 처리 (400 Bad Request)
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<WebtoonTriggerResponse> handleBadRequestException(BadRequestException ex) {
        log.error("Bad Request Exception: {}", ex.getMessage());
        
        WebtoonTriggerResponse response = WebtoonTriggerResponse.builder()
            .message(ex.getMessage())
            .processedCount(0)
            .status("ERROR")
            .timestamp(System.currentTimeMillis())
            .build();
            
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * IllegalArgumentException 처리 (400 Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<WebtoonTriggerResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal Argument Exception: {}", ex.getMessage());
        
        WebtoonTriggerResponse response = WebtoonTriggerResponse.builder()
            .message(ex.getMessage())
            .processedCount(0)
            .status("ERROR")
            .timestamp(System.currentTimeMillis())
            .build();
            
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * NumberFormatException 처리 (400 Bad Request)
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<WebtoonTriggerResponse> handleNumberFormatException(NumberFormatException ex) {
        log.error("Number Format Exception: {}", ex.getMessage());
        
        WebtoonTriggerResponse response = WebtoonTriggerResponse.builder()
            .message("잘못된 숫자 형식입니다: " + ex.getMessage())
            .processedCount(0)
            .status("ERROR")
            .timestamp(System.currentTimeMillis())
            .build();
            
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 일반 Exception 처리 (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebtoonTriggerResponse> handleException(Exception ex) {
        log.error("Unexpected Exception: {}", ex.getMessage(), ex);
        
        WebtoonTriggerResponse response = WebtoonTriggerResponse.builder()
            .message("서버 내부 오류가 발생했습니다: " + ex.getMessage())
            .processedCount(0)
            .status("ERROR")
            .timestamp(System.currentTimeMillis())
            .build();
            
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 