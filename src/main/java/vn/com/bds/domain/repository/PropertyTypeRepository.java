package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.PropertyType;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // <-- Import

public interface PropertyTypeRepository {
    List<PropertyType> findAll();
    List<PropertyType> findByListingTypeId(UUID listingTypeId); // <-- Change to UUID
    Optional<PropertyType> findById(UUID id); // <-- Change to UUID
    PropertyType save(PropertyType propertyType);
    void deleteById(UUID id); // <-- Change to UUID
    boolean existsById(UUID id); // <-- Change to UUID
}