package vn.com.bds.presentation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private UserDto user; // <-- THÊM TRƯỜNG NÀY
}