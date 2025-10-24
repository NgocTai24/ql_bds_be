package vn.com.bds.infrastructure.repository.entity;


import jakarta.persistence.*;
import lombok.Data;
import vn.com.bds.domain.model.PostStatus; // Import Enum

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // <-- Bảo DB tự sinh UUID
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Lob // Dành cho text dài
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, precision = 19, scale = 4) // Dùng Decimal cho tiền tệ
    private BigDecimal price;

    private Double squareMeters;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    // --- CÁC MỐI QUAN HỆ ---

    // Nhiều bài Post thuộc 1 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // Nhiều bài Post thuộc 1 ListingType (Bán/Cho thuê)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_type_id", nullable = false)
    private ListingTypeEntity listingType;

    // Nhiều bài Post thuộc 1 PropertyType (Căn hộ/Đất nền)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_type_id", nullable = false)
    private PropertyTypeEntity propertyType;

    // Một bài Post có nhiều Image
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // KIỂM TRA DÒNG NÀY
    private List<ImageEntity> images;

    // Một bài Post có nhiều Comment
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

    // Một bài Post có nhiều Rating
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingEntity> ratings;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }


}