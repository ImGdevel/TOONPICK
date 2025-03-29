package toonpick.exception;

import toonpick.type.ErrorCode;

/**
 * 만료되어 유효하지 않는 JWT Token 예외
 */
public class ExpiredJwtTokenException extends JwtException {
    public ExpiredJwtTokenException(String message) {
        super(message);
    }

    public ExpiredJwtTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public ExpiredJwtTokenException(String message, String resource) {
        super(String.format("%s : %s", message, resource));
    }

    public ExpiredJwtTokenException(ErrorCode errorCode, String resource) {
        super(String.format("%s : %s", errorCode.getMessage(), resource));
    }

    public ExpiredJwtTokenException(ErrorCode errorCode, Long id) {
        super(String.format("%s : %s", errorCode.getMessage(), id.toString()));
    }
}
