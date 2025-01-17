package toonpick.app.exception.exception;

public class ExpiredJwtTokenException extends JwtException {
    public ExpiredJwtTokenException(String message) {
        super(message);
    }
}
