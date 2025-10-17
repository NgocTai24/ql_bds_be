package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.Rating;
import vn.com.bds.infrastructure.repository.entity.RatingEntity;

public class RatingMapper {

    public static Rating toDomain(RatingEntity entity) {
        if (entity == null) return null;
        return Rating.builder()
                .id(entity.getId())
                .stars(entity.getStars())
                .createdAt(entity.getCreatedAt())
                .user(UserMapper.toDomain(entity.getUser()))
                .post(PostMapper.toDomain(entity.getPost()))
                .build();
    }

    public static RatingEntity toEntity(Rating model) {
        if (model == null) return null;
        RatingEntity entity = new RatingEntity();
        entity.setId(model.getId());
        entity.setStars(model.getStars());
        entity.setCreatedAt(model.getCreatedAt());
        if (model.getUser() != null) {
            entity.setUser(UserMapper.toEntity(model.getUser()));
        }
        if (model.getPost() != null) {
            entity.setPost(PostMapper.toEntity(model.getPost()));
        }
        return entity;
    }
}