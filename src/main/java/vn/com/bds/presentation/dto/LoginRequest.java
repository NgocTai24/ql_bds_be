package vn.com.bds.presentation.dto;

import lombok.Data;

// Dùng để nhận JSON đăng nhập
@Data
public class LoginRequest {
    private String email;
    private String password;
}