package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Post;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // <-- Import

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(UUID id);
    List<Post> findAll(); // Simple version for now
    void deleteById(UUID id);
    boolean existsById(UUID id); // Add check
    // You might add methods like findByUserId later
}