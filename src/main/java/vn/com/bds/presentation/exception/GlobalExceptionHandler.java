package vn.com.bds.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException; // Lỗi 403
import org.springframework.web.HttpRequestMethodNotSupportedException; // Lỗi 405
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException; // Lỗi 404
import vn.com.bds.domain.exception.ResourceNotFoundException;
import vn.com.bds.presentation.dto.ApiResponse;

@RestControllerAdvice // Báo cho Spring biết đây là trạm xử lý lỗi tập trung
public class GlobalExceptionHandler {

    // 1. Xử lý 404 (Khi data không tìm thấy trong DB)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 2. Xử lý 404 (Khi gõ sai URL API)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ApiResponse.error(HttpStatus.NOT_FOUND, "API endpoint not found: " + ex.getResourcePath());
    }

    // 3. Xử lý 403 (Token sai, không có quyền truy cập)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return ApiResponse.error(HttpStatus.FORBIDDEN, "Access denied. You do not have permission.");
    }

    // 4. Xử lý 405 (Dùng GET thay vì POST, v.v.)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ApiResponse.error(HttpStatus.METHOD_NOT_ALLOWED,
                "Method not supported: " + ex.getMethod() + ". Supported methods are: " + ex.getSupportedHttpMethods());
    }

    // 5. Xử lý 500 (Tất cả các lỗi logic/runtime khác)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex, WebRequest request) {
        // Ghi log lỗi chi tiết ở đây (quan trọng cho debug)
        ex.printStackTrace(); // Dùng logger tốt hơn trong production

        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred: " + ex.getMessage());
    }
}