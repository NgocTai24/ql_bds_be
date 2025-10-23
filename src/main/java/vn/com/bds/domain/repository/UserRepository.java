package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);
    void deleteById(UUID id); // <-- THÊM HÀM NÀY
    boolean existsById(UUID id); // <-- Thêm hàm kiểm tra tồn tại (hữu ích)

    List<User> findAll();

    int count();
}