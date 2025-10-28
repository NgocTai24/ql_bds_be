package vn.com.bds.presentation.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateCommentRequest {
    private String content;
    private UUID postId;
    private UUID parentId; // Optional: for replies
}