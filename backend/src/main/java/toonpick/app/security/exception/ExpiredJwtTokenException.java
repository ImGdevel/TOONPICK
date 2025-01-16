package toonpick.app.security.exception;

public class ExpiredJwtTokenException extends JwtException {
    public ExpiredJwtTokenException(String message) {
        super(message);
    }
}
