package vn.com.bds.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import vn.com.bds.domain.model.Role;
import vn.com.bds.domain.model.Role;

import java.time.Instant;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String avatarUrl; // Đường dẫn ảnh từ Cloudinary

    @Enumerated(EnumType.STRING) // Lưu Enum dưới dạng String
    @Column(nullable = false)
    private Role role;

    private Instant createdAt;

    // (Các mối quan hệ như @OneToMany với PostEntity sẽ thêm ở đây)

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}