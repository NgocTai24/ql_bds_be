package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.News;
import vn.com.bds.infrastructure.repository.entity.NewsEntity;

public class NewsMapper {

    public static News toDomain(NewsEntity entity) {
        if (entity == null) return null;
        return News.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrl(entity.getImageUrl())
                .slug(entity.getSlug())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .author(UserMapper.toDomain(entity.getAuthor()))
                .build();
    }

    public static NewsEntity toEntity(News model) {
        if (model == null) return null;
        NewsEntity entity = new NewsEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setContent(model.getContent());
        entity.setImageUrl(model.getImageUrl());
        entity.setSlug(model.getSlug());
        entity.setStatus(model.getStatus());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());
        if (model.getAuthor() != null) {
            entity.setAuthor(UserMapper.toEntity(model.getAuthor()));
        }
        return entity;
    }
}