package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.ListingType;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // <-- Import

public interface ListingTypeRepository {
    List<ListingType> findAll();
    Optional<ListingType> findById(UUID id); // <-- Sửa thành UUID
    ListingType save(ListingType listingType);
    void deleteById(UUID id); // <-- Sửa thành UUID
    boolean existsById(UUID id); // <-- Sửa thành UUID
}