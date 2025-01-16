package toonpick.app.security.exception;

public class InvalidJwtTokenException extends JwtException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
