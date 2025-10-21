package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity;

import java.util.List;

public interface PropertyTypeSpringDataRepository extends JpaRepository<PropertyTypeEntity, Integer> {

    // Spring Data JPA sẽ tự động tạo query
    // để tìm các PropertyTypeEntity dựa trên trường listingType.id
    List<PropertyTypeEntity> findByListingTypeId(Integer listingTypeId);
}