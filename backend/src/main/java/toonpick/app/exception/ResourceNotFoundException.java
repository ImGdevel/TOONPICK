package toonpick.app.exception;

/**
 * 요청한 자원을 찾을 수 없음
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public ResourceNotFoundException(String message, String resource) {
        super(String.format("%s : %s", message, resource));
    }

    public ResourceNotFoundException(ErrorCode errorCode, String resource) {
        super(String.format("%s : %s", errorCode.getMessage(), resource));
    }

        public ResourceNotFoundException(ErrorCode errorCode, Long id) {
        super(String.format("%s : %s", errorCode.getMessage(), id.toString()));
    }
}
