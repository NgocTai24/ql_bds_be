package vn.com.bds.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// JsonInclude.Include.NON_NULL: Sẽ chỉ bao gồm các trường không null trong JSON
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int statusCode;
    private String status;
    private String message;
    private T data; // Dữ liệu generic (có thể là token, user, list post...)

    // --- Các hàm tiện ích để tạo response nhanh ---

    // 1. Thành công (có data)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .status("success")
                .message(message)
                .data(data)
                .build());
    }

    // 2. Thành công (chỉ thông báo, ví dụ: Delete)
    public static <T> ResponseEntity<ApiResponse<T>> success(String message) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .status("success")
                .message(message)
                .build());
    }

    // 3. Tạo mới thành công (Created 201)
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return new ResponseEntity<>(
                ApiResponse.<T>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .status("success")
                        .message(message)
                        .data(data)
                        .build(),
                HttpStatus.CREATED);
    }

    // 4. Lỗi (Dùng cho Exception Handler)
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return new ResponseEntity<>(
                ApiResponse.<T>builder()
                        .statusCode(status.value())
                        .status("error")
                        .message(message)
                        .build(),
                status);
    }
}