package com.toonpick.exception;

/**
 * JWT 토큰 예외
 */
public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
