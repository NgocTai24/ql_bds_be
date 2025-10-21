package vn.com.bds.domain.exception;

// Đây là Runtime Exception, không cần try-catch
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}