package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.Image;
import vn.com.bds.infrastructure.repository.entity.ImageEntity;
import vn.com.bds.infrastructure.repository.entity.PostEntity;

public class ImageMapper {

    // Map Entity to Domain (only map parent Post if really needed, often avoided here)
    public static Image toDomain(ImageEntity entity) {
        if (entity == null) return null;
        return Image.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                // .post(PostMapper.toDomain(entity.getPost())) // Avoid if possible
                .build();
    }

    // Map Domain to Entity (Need PostEntity for saving)
    public static ImageEntity toEntity(Image model, PostEntity postEntity) {
        if (model == null) return null;
        return ImageEntity.builder()
                .id(model.getId()) // ID might be null for new images
                .imageUrl(model.getImageUrl())
                .post(postEntity) // Set the relationship
                .build();
    }
}