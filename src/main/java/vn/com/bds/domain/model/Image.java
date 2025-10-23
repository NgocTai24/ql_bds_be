package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Image {
    private UUID id;
    private String imageUrl;

    // Hình ảnh này thuộc về bài đăng nào
    private Post post;
}