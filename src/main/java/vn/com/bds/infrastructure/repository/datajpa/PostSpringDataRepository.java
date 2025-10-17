package vn.com.bds.infrastructure.repository.datajpa;


import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.PostEntity;

public interface PostSpringDataRepository extends JpaRepository<PostEntity, Long> {
    // (Sau này có thể thêm các query phức tạp ở đây)
}