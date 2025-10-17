package vn.com.bds.infrastructure.repository.adapter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.bds.domain.model.ListingType;
import vn.com.bds.domain.repository.ListingTypeRepository;
import vn.com.bds.infrastructure.repository.datajpa.ListingTypeSpringDataRepository;
import vn.com.bds.infrastructure.repository.mapper.ListingTypeMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ListingTypeRepositoryAdapter implements ListingTypeRepository {

    private final ListingTypeSpringDataRepository springDataRepository;

    @Override
    public Optional<ListingType> findById(Integer id) {
        return springDataRepository.findById(id).map(ListingTypeMapper::toDomain);
    }

    @Override
    public List<ListingType> findAll() {
        return springDataRepository.findAll().stream()
                .map(ListingTypeMapper::toDomain)
                .collect(Collectors.toList());
    }
}