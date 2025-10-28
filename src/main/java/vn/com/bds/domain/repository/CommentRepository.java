package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Comment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {
    Comment save(Comment comment);
    // Find top-level comments for a post (parent is null)
    List<Comment> findByPostIdAndParentIsNull(UUID postId);
    // Find a specific comment by its ID
    Optional<Comment> findById(UUID id);
    // Delete a comment
    void delete(Comment comment); // Pass the model for potential checks before deleting
    // Maybe add: List<Comment> findByParentId(UUID parentId) later for replies
}