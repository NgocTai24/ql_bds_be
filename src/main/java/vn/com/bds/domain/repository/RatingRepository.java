package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingRepository {
    Rating save(Rating rating);

    // Kiểm tra xem user đã rating bài post này chưa
    Optional<Rating> findByUserAndPost(Long userId, Long postId);

    // Lấy tất cả rating của một bài post
    List<Rating> findByPostId(Long postId);
}