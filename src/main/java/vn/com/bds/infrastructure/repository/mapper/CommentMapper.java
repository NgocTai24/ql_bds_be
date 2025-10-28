package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.Comment;
import vn.com.bds.infrastructure.repository.entity.CommentEntity;
import vn.com.bds.infrastructure.repository.entity.PostEntity;
import vn.com.bds.infrastructure.repository.entity.UserEntity;

import java.util.stream.Collectors; // Import

public class CommentMapper {

    public static Comment toDomain(CommentEntity entity) {
        if (entity == null) return null;
        return Comment.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .user(UserMapper.toDomain(entity.getUser()))
                .post(PostMapper.toDomain(entity.getPost())) // <-- BỎ COMMENT DÒNG NÀY
                .parent(entity.getParent() != null ? Comment.builder().id(entity.getParent().getId()).build() : null)
                .build();
    }

    public static CommentEntity toEntity(Comment model) {
        if (model == null) return null;
        CommentEntity entity = new CommentEntity();
        entity.setId(model.getId());
        entity.setContent(model.getContent());
        entity.setCreatedAt(model.getCreatedAt());

        // Set relationships based on IDs provided via UseCase/Controller
        if (model.getUser() != null && model.getUser().getId() != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(model.getUser().getId()); // Set only ID for relationship
            entity.setUser(userEntity);
        }
        if (model.getPost() != null && model.getPost().getId() != null) {
            PostEntity postEntity = new PostEntity();
            postEntity.setId(model.getPost().getId()); // Set only ID for relationship
            entity.setPost(postEntity);
        }
        if (model.getParent() != null && model.getParent().getId() != null) {
            CommentEntity parentEntity = new CommentEntity();
            parentEntity.setId(model.getParent().getId()); // Set only ID for relationship
            entity.setParent(parentEntity);
        }
        return entity;
    }
}