package vn.com.bds.infrastructure.repository.datajpa;


import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity;

public interface ListingTypeSpringDataRepository extends JpaRepository<ListingTypeEntity, Integer> {
}