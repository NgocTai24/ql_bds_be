package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    private String fullname;
    private String email;
    private String password;
    private String phone;
    private String avatarUrl;
    private Role role;
    private Instant createdAt;

    // Các mối quan hệ
    private List<Post> posts;
    private List<News> news;
    private List<Comment> comments;
    private List<Rating> ratings;
}