package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {
    Comment save(Comment comment);

    // Lấy tất cả comment (cấp 1) của một bài post
    List<Comment> findByPostIdAndParentIsNull(UUID postId);

    Optional<Comment> findById(UUID id);
    void deleteById(UUID id);
}