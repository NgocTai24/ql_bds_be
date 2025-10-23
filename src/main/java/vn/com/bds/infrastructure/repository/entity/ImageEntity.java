package vn.com.bds.infrastructure.repository.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // <-- Bảo DB tự sinh UUID
    private UUID id;

    @Column(nullable = false)
    private String imageUrl; // URL từ Cloudinary

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;
}
