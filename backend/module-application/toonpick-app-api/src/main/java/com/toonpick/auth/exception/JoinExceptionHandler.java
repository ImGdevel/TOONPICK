package com.toonpick.auth.exception;

import com.toonpick.auth.controller.JoinController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = JoinController.class)
public class JoinExceptionHandler {

    final private Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    @ExceptionHandler(JoinValidationException.class)
    public ResponseEntity<String> handleJoinValidationException(JoinValidationException ex) {
        logger.error("join failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
