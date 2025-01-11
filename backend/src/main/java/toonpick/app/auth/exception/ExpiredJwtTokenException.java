package toonpick.app.auth.exception;

public class ExpiredJwtTokenException extends JwtException {
    public ExpiredJwtTokenException(String message) {
        super(message);
    }
}
