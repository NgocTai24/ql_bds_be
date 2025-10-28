package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.domain.model.Comment;

import java.util.List;
import java.util.UUID;

public interface ManageCommentUseCase {
    // Read
    List<Comment> getCommentsByPost(UUID postId);

    // Write
    Comment createComment(CreateCommentCommand command);
    void deleteComment(DeleteCommentCommand command);

    @Value @Builder
    class CreateCommentCommand {
        String content;
        UUID postId;
        UUID parentId; // Optional: ID of the comment being replied to
        String userEmail; // Email of the commenter (from token)
    }

    @Value @Builder
    class DeleteCommentCommand {
        UUID commentId;
        String userEmail; // Email of the user attempting deletion (from token)
    }
}