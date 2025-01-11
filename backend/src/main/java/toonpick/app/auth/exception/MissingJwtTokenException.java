package toonpick.app.auth.exception;

public class MissingJwtTokenException extends JwtException {
    public MissingJwtTokenException(String message) {
        super(message);
    }
}
