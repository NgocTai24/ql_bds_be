package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

// Model cho Đánh giá (bảng ratings)
@Data
@Builder
public class Rating {
    private Long id;
    private int stars; // Số sao (ví dụ: 1, 2, 3, 4, 5)
    private Instant createdAt;

    // Mối quan hệ: Ai đánh giá
    private User user;

    // Mối quan hệ: Đánh giá cho bài đăng nào
    private Post post;
}