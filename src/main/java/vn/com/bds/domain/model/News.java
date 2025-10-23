package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.NewsStatus;

import java.time.Instant;
import java.util.UUID;

// Model cho Tin tức (bảng news)
@Data
@Builder
public class News {
    private UUID id;
    private String title;
    private String content; // Nội dung tin tức (có thể là HTML/Markdown)
    private String imageUrl; // Ảnh bìa của tin tức
    private String slug; // Đường dẫn thân thiện (ví dụ: /tin-tuc/thi-truong-bds-thang-10)
    private NewsStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    // Mối quan hệ: Tin tức này do ai viết
    private User author;
}