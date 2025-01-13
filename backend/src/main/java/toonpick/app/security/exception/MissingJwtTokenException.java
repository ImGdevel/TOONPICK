package toonpick.app.security.exception;

public class MissingJwtTokenException extends JwtException {
    public MissingJwtTokenException(String message) {
        super(message);
    }
}
