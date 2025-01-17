package toonpick.app.exception.exception;

public class MissingJwtTokenException extends JwtException {
    public MissingJwtTokenException(String message) {
        super(message);
    }
}
