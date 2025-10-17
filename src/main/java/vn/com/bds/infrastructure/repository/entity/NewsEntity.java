package vn.com.bds.infrastructure.repository.entity;



import jakarta.persistence.*;
import lombok.Data;
import vn.com.bds.domain.model.NewsStatus;

import java.time.Instant;

@Data
@Entity
@Table(name = "news")
public class NewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    private String imageUrl;

    @Column(unique = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    private NewsStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // user_id hoặc author_id
    private UserEntity author;
}