package vn.com.bds.infrastructure.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <-- IMPORT THIS
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity;
import java.util.List;
import java.util.UUID;

public interface PropertyTypeSpringDataRepository extends JpaRepository<PropertyTypeEntity, UUID> {
    List<PropertyTypeEntity> findByListingTypeId(UUID listingTypeId);

    // --- ADD THIS METHOD ---
    @Query("SELECT pt FROM PropertyTypeEntity pt JOIN FETCH pt.listingType")
    List<PropertyTypeEntity> findAllWithListingType();
    // -----------------------
}