package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity;
import java.util.UUID; // <-- Import

// Sửa kiểu ID thành UUID
public interface ListingTypeSpringDataRepository extends JpaRepository<ListingTypeEntity, UUID> {
}