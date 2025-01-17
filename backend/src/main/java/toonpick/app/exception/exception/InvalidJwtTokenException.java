package toonpick.app.exception.exception;

public class InvalidJwtTokenException extends JwtException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
