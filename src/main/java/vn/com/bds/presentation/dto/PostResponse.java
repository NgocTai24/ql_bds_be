package vn.com.bds.presentation.dto;


import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.Post;

import java.time.Instant;
import java.util.UUID;

// DTO này định nghĩa JSON trả về cho client
@Data
@Builder
public class PostResponse {
    private UUID id;
    private String title;
    private String address;
    private java.math.BigDecimal price;
    private String status;
    private Instant createdAt;
    private String authorName;

    // Mapper để chuyển từ Domain Model sang DTO Response
    public static PostResponse fromModel(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .price(post.getPrice())
                .status(post.getStatus().name())
                .createdAt(post.getCreatedAt())
                .authorName(post.getUser() != null ? post.getUser().getFullname() : null)
                .build();
    }
}
