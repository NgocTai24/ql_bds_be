package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.ImageEntity;
import java.util.UUID;

public interface ImageSpringDataRepository extends JpaRepository<ImageEntity, UUID> {
}