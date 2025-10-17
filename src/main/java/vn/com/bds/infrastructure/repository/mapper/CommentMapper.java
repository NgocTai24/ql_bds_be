package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.Comment;
import vn.com.bds.infrastructure.repository.entity.CommentEntity;

public class CommentMapper {

    public static Comment toDomain(CommentEntity entity) {
        if (entity == null) return null;
        return Comment.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .user(UserMapper.toDomain(entity.getUser()))
                .post(PostMapper.toDomain(entity.getPost()))
                // Map quan hệ cha (nếu có)
                .parent(toDomain(entity.getParent())) // Đệ quy
                .build();
    }

    public static CommentEntity toEntity(Comment model) {
        if (model == null) return null;
        CommentEntity entity = new CommentEntity();
        entity.setId(model.getId());
        entity.setContent(model.getContent());
        entity.setCreatedAt(model.getCreatedAt());
        if (model.getUser() != null) {
            entity.setUser(UserMapper.toEntity(model.getUser()));
        }
        if (model.getPost() != null) {
            entity.setPost(PostMapper.toEntity(model.getPost()));
        }
        if (model.getParent() != null) {
            entity.setParent(toEntity(model.getParent())); // Đệ quy
        }
        return entity;
    }
}