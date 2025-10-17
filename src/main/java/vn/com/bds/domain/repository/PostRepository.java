package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {

    // Đảm bảo bạn có 4 phương thức này
    // với CHÍNH XÁC tên, kiểu trả về, và tham số

    Post save(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll(); // <-- Khớp với dòng 35

    void deleteById(Long id); // <-- Khớp với dòng 43
}