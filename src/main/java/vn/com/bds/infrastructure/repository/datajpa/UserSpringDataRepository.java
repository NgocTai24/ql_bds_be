package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.UserEntity;
import java.util.Optional;

public interface UserSpringDataRepository extends JpaRepository<UserEntity, Long> {
    // Spring Data JPA tự động tạo query từ tên phương thức
    Optional<UserEntity> findByEmail(String email);
}