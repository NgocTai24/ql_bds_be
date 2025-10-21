package vn.com.bds.presentation.dto;

import lombok.Data;

// Dùng để nhận JSON đăng ký
@Data
public class RegisterRequest {
    private String fullname;
    private String email;
    private String password;
    private String phone;
}