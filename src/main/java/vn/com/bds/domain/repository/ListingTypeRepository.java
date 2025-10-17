package vn.com.bds.domain.repository;

import vn.com.bds.domain.model.ListingType;
import java.util.List;
import java.util.Optional;

public interface ListingTypeRepository {
    Optional<ListingType> findById(Integer id);
    List<ListingType> findAll();
}