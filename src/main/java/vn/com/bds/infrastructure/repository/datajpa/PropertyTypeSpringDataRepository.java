package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity;
import java.util.List;
import java.util.UUID; // <-- Import

// Change generic type to UUID
public interface PropertyTypeSpringDataRepository extends JpaRepository<PropertyTypeEntity, UUID> {
    // Change parameter type to UUID
    List<PropertyTypeEntity> findByListingTypeId(UUID listingTypeId);
}