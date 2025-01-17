package toonpick.app.exception;

/**
 * 데이터베이스 작업 중 발생한 예외
 */
public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseOperationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
