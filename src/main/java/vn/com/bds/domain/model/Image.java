package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Image {
    private Long id;
    private String imageUrl;

    // Hình ảnh này thuộc về bài đăng nào
    private Post post;
}