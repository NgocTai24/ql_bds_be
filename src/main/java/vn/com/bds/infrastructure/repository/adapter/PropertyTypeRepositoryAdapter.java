package vn.com.bds.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.bds.domain.model.PropertyType;
import vn.com.bds.domain.repository.PropertyTypeRepository;
import vn.com.bds.infrastructure.repository.datajpa.PropertyTypeSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity; // Import Entity
import vn.com.bds.infrastructure.repository.mapper.PropertyTypeMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PropertyTypeRepositoryAdapter implements PropertyTypeRepository {

    private final PropertyTypeSpringDataRepository springDataRepository;

    @Override
    public List<PropertyType> findAll() {
        return springDataRepository.findAll().stream()
                .map(PropertyTypeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyType> findByListingTypeId(UUID listingTypeId) {
        // --- PUT IMPLEMENTATION BACK ---
        return springDataRepository.findByListingTypeId(listingTypeId).stream()
                .map(PropertyTypeMapper::toDomain)
                .collect(Collectors.toList());
        // -----------------------------
    }

    @Override
    public Optional<PropertyType> findById(UUID id) {
        // --- PUT IMPLEMENTATION BACK ---
        return springDataRepository.findById(id).map(PropertyTypeMapper::toDomain);
        // -----------------------------
    }

    @Override
    public PropertyType save(PropertyType propertyType) {
        // --- PUT IMPLEMENTATION BACK ---
        PropertyTypeEntity entity = PropertyTypeMapper.toEntity(propertyType);
        PropertyTypeEntity savedEntity = springDataRepository.save(entity);
        return PropertyTypeMapper.toDomain(savedEntity);
        // -----------------------------
    }

    @Override
    public void deleteById(UUID id) {
        // --- PUT IMPLEMENTATION BACK ---
        springDataRepository.deleteById(id);
        // -----------------------------
    }

    @Override
    public boolean existsById(UUID id) {
        // --- PUT IMPLEMENTATION BACK ---
        return springDataRepository.existsById(id);
        // -----------------------------
    }
}