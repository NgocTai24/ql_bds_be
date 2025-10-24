package vn.com.bds.infrastructure.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor; // Add constructor if needed by JPA
import lombok.AllArgsConstructor; // Add constructor if needed by JPA
import lombok.Builder; // Add builder if useful
import java.util.UUID;

@Data
@Entity
@Table(name = "images")
@Builder // Add builder
@NoArgsConstructor // Required by JPA
@AllArgsConstructor // Optional, convenient
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String imageUrl; // URL from Cloudinary

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;
}