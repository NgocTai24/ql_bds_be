package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// Model cho Bình luận (bảng comments)
@Data
@Builder
public class Comment {
    private UUID id;
    private String content; // Nội dung bình luận
    private Instant createdAt;

    // Mối quan hệ: Ai bình luận
    private User user;

    // Mối quan hệ: Bình luận cho bài đăng nào
    private Post post;

    // Mối quan hệ tự thân để làm comment cha-con
    private Comment parent; // Bình luận này đang trả lời cho bình luận nào
    private List<Comment> replies; // Danh sách các bình luận trả lời nó
}