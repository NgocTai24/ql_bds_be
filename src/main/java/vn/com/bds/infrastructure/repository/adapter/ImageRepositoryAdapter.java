package vn.com.bds.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.bds.domain.model.Image;
import vn.com.bds.domain.repository.ImageRepository;
import vn.com.bds.infrastructure.repository.datajpa.ImageSpringDataRepository;
import vn.com.bds.infrastructure.repository.entity.ImageEntity;
import vn.com.bds.infrastructure.repository.entity.PostEntity; // Need PostEntity for mapping
import vn.com.bds.infrastructure.repository.mapper.ImageMapper;
import vn.com.bds.infrastructure.repository.mapper.PostMapper; // Need PostMapper

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryAdapter implements ImageRepository {

    private final ImageSpringDataRepository springDataRepository;
    // We might need PostRepository/Mapper if the Image model needs the Post object later

    @Override
    public Image save(Image image) {
        // Saving a single image requires knowing its Post
        // This logic is better placed within the Post save operation
        throw new UnsupportedOperationException("Saving single image is not directly supported, save via Post");
    }

    @Override
    public List<Image> saveAll(List<Image> images) {
        // This assumes the PostEntity is already set correctly *before* calling saveAll
        // Typically done within the Post UseCase/Service
        List<ImageEntity> entitiesToSave = images.stream()
                .map(img -> ImageMapper.toEntity(img, null)) // Temporary null PostEntity
                .collect(Collectors.toList());

        // We need to re-fetch or correctly set PostEntity before saving
        // This highlights why image saving is coupled with Post saving
        throw new UnsupportedOperationException("Saving images requires linking to PostEntity context");

        // --- Correct implementation requires Post context ---
        // List<ImageEntity> savedEntities = springDataRepository.saveAll(entitiesToSave);
        // return savedEntities.stream().map(ImageMapper::toDomain).collect(Collectors.toList());
    }
}