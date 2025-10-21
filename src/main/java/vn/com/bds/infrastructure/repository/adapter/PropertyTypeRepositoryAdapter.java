package vn.com.bds.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.bds.domain.model.PropertyType;
import vn.com.bds.domain.repository.PropertyTypeRepository;
import vn.com.bds.infrastructure.repository.datajpa.PropertyTypeSpringDataRepository;
import vn.com.bds.infrastructure.repository.mapper.PropertyTypeMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository // <-- Báo cho Spring biết đây là "nhân viên"
@RequiredArgsConstructor
public class PropertyTypeRepositoryAdapter implements PropertyTypeRepository {

    private final PropertyTypeSpringDataRepository springDataRepository;
    // (Lombok sẽ tự động inject bean này)

    @Override
    public Optional<PropertyType> findById(Integer id) {
        return springDataRepository.findById(id)
                .map(PropertyTypeMapper::toDomain);
    }

    @Override
    public List<PropertyType> findByListingTypeId(Integer listingTypeId) {
        // Gọi hàm JPA, sau đó map kết quả từ List<Entity> sang List<Model>
        return springDataRepository.findByListingTypeId(listingTypeId).stream()
                .map(PropertyTypeMapper::toDomain)
                .collect(Collectors.toList());
    }
}