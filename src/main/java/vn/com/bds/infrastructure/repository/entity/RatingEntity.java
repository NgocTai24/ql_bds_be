package vn.com.bds.infrastructure.repository.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "ratings", uniqueConstraints = {
        // Đảm bảo 1 user chỉ rate 1 post 1 lần
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class RatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // <-- Bảo DB tự sinh UUID
    private UUID id;

    @Column(nullable = false)
    private int stars;

    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}