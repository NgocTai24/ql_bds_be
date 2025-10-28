package vn.com.bds.presentation.dto;

import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.Comment; // Import domain model
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class CommentDto {
    private UUID id;
    private String content;
    private Instant createdAt;
    private UserDto user;
    private UUID postId;   // Will be populated now
    private UUID parentId; // Will be populated now
    private List<CommentDto> replies;


    public static CommentDto fromModel(Comment model) {
        if (model == null) return null;

        // --- CORRECTED ID EXTRACTION ---
        UUID postIdValue = null;
        if (model.getPost() != null && model.getPost().getId() != null) {
            postIdValue = model.getPost().getId();
        }

        UUID parentIdValue = null;
        if (model.getParent() != null && model.getParent().getId() != null) {
            parentIdValue = model.getParent().getId();
        }
        // --- END CORRECTION ---

        return CommentDto.builder()
                .id(model.getId())
                .content(model.getContent())
                .createdAt(model.getCreatedAt())
                .user(UserDto.fromModel(model.getUser())) // Assuming UserDto.fromModel handles null user
                .postId(postIdValue)     // Use the extracted postId
                .parentId(parentIdValue) // Use the extracted parentId
                // --- THÊM LOGIC MAP REPLIES ---
                .replies(model.getReplies() != null ? model.getReplies().stream()
                        .map(CommentDto::fromModel) // Gọi đệ quy fromModel
                        .collect(Collectors.toList()) : Collections.emptyList()) // Trả list rỗng nếu null
                // ---------------------------
                .build();
    }
}