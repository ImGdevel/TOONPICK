package toonpick.app.auth.exception;

public class InvalidJwtTokenException extends JwtException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
