package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
    // ... các phương thức cần thiết khác
}