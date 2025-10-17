package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

// Đây là bài đăng BĐS
@Data
@Builder
public class Post {
    private Long id;
    private String title;
    private String description;
    private String address;
    private BigDecimal price; // Dùng BigDecimal cho tiền tệ
    private Double squareMeters; // Diện tích
    private vn.com.bds.domain.model.PostStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    // Các mối quan hệ
    private User user; // Bài đăng này của ai
    private ListingType listingType; // Là Bán hay Cho thuê
    private PropertyType propertyType; // Là loại nhà gì
    private List<Image> images; // Danh sách hình ảnh
}