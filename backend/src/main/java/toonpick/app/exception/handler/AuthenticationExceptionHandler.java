package toonpick.app.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toonpick.app.exception.exception.UsernameAlreadyExistsException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        LOGGER.error("Username not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found: " + ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        LOGGER.error("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access is denied. " + ex.getMessage());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthenticationCredentialsNotFoundException(
            AuthenticationCredentialsNotFoundException ex) {
        LOGGER.error("Authentication credentials not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication credentials are required.");
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        LOGGER.error("Username already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists: " + ex.getMessage());
    }

    @ExceptionHandler(InvalidCsrfTokenException.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidCsrfTokenException ex) {
        LOGGER.error("Invalid Token: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid CSRF token: " + ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        LOGGER.error("Null pointer exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        LOGGER.error("Unexpected error in authentication: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
