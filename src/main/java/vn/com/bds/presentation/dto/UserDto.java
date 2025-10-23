package vn.com.bds.presentation.dto;

import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.Role;
import vn.com.bds.domain.model.User;

import java.util.UUID;

@Data
@Builder
public class UserDto {
    private UUID id;
    private String fullname;
    private String email;
    private String phone;
    private String avatarUrl;
    private Role role;

    // Hàm factory để chuyển đổi từ Model sang DTO
    public static UserDto fromModel(User user) {
        return UserDto.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .build();
    }
}