package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.Rating;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository {
    Rating save(Rating rating);

    // Kiểm tra xem user đã rating bài post này chưa
    Optional<Rating> findByUserAndPost(UUID userId, UUID postId);

    // Lấy tất cả rating của một bài post
    List<Rating> findByPostId(UUID postId);
}