package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Post;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // <-- Import

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(UUID id); // <-- Sửa thành UUID
    List<Post> findAll();
    void deleteById(UUID id); // <-- Sửa thành UUID
    // boolean existsById(UUID id); // <-- Thêm nếu cần
}