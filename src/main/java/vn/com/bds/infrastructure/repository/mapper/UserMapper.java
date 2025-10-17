package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.User;
import vn.com.bds.infrastructure.repository.entity.UserEntity;

// Lớp này không phải là @Bean, nó chỉ chứa các phương thức static
public class UserMapper {

    // Chuyển từ Entity (database) sang Model (domain)
    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .id(entity.getId())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .phone(entity.getPhone())
                .avatarUrl(entity.getAvatarUrl())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                // Không map các list quan hệ ở đây để tránh vòng lặp vô hạn
                .build();
    }

    // Chuyển từ Model (domain) sang Entity (database)
    public static UserEntity toEntity(User model) {
        if (model == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(model.getId()); // Cần cho việc update
        entity.setFullname(model.getFullname());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setPhone(model.getPhone());
        entity.setAvatarUrl(model.getAvatarUrl());
        entity.setRole(model.getRole());
        entity.setCreatedAt(model.getCreatedAt());
        return entity;
    }
}