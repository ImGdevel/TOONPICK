package com.toonpick.internal.security.handler;

import com.toonpick.common.exception.AuthenticationException;
import com.toonpick.internal.security.exception.ExpiredJwtTokenException;
import com.toonpick.internal.security.exception.InvalidJwtTokenException;
import com.toonpick.internal.security.exception.MissingJwtTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class JwtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(JwtExceptionHandler.class);

    @ExceptionHandler({
        MissingJwtTokenException.class,
        InvalidJwtTokenException.class,
        ExpiredJwtTokenException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleJwtException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
