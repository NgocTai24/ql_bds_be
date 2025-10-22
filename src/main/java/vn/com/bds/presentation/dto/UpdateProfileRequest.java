package vn.com.bds.presentation.dto;

import lombok.Data;

// DTO này nhận JSON body từ client khi họ muốn update
@Data
public class UpdateProfileRequest {
    private String fullname;
    private String phone;
}