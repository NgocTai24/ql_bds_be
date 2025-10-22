package vn.com.bds.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.bds.domain.model.ListingType;
import vn.com.bds.domain.repository.ListingTypeRepository;
import vn.com.bds.infrastructure.repository.datajpa.ListingTypeSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity; // Import Entity
import vn.com.bds.infrastructure.repository.mapper.ListingTypeMapper;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ListingTypeRepositoryAdapter implements ListingTypeRepository {

    private final ListingTypeSpringDataRepository springDataRepository;

    @Override
    public Optional<ListingType> findById(UUID id) {
        return springDataRepository.findById(id).map(ListingTypeMapper::toDomain);
    }

    @Override
    public List<ListingType> findAll() {
        return springDataRepository.findAll().stream()
                .map(ListingTypeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public ListingType save(ListingType listingType) {
        // --- PUT IMPLEMENTATION BACK ---
        ListingTypeEntity entity = ListingTypeMapper.toEntity(listingType);
        ListingTypeEntity savedEntity = springDataRepository.save(entity);
        return ListingTypeMapper.toDomain(savedEntity);
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