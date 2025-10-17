package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.PropertyType;
import java.util.List;
import java.util.Optional;

public interface PropertyTypeRepository {
    Optional<PropertyType> findById(Integer id);
    List<PropertyType> findByListingTypeId(Integer listingTypeId);
}