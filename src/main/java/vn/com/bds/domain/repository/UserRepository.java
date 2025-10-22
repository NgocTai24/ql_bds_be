package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
    void deleteById(Long id); // <-- THÊM HÀM NÀY
    boolean existsById(Long id); // <-- Thêm hàm kiểm tra tồn tại (hữu ích)

    List<User> findAll();
}