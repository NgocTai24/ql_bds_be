package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    // Lấy tất cả comment (cấp 1) của một bài post
    List<Comment> findByPostIdAndParentIsNull(Long postId);

    void delete(Comment comment);
}