package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.Image;
import vn.com.bds.infrastructure.repository.entity.ImageEntity;

public class ImageMapper {

    public static Image toDomain(ImageEntity entity) {
        if (entity == null) return null;
        return Image.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                // Map quan hệ @ManyToOne (post)
                .post(PostMapper.toDomain(entity.getPost()))
                .build();
    }

    public static ImageEntity toEntity(Image model) {
        if (model == null) return null;
        ImageEntity entity = new ImageEntity();
        entity.setId(model.getId());
        entity.setImageUrl(model.getImageUrl());
        // Map quan hệ @ManyToOne (post)
        if (model.getPost() != null) {
            entity.setPost(PostMapper.toEntity(model.getPost()));
        }
        return entity;
    }
}