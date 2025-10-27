package vn.com.bds.infrastructure.repository.mapper;


import vn.com.bds.domain.model.Post;
import vn.com.bds.infrastructure.repository.entity.PostEntity;

import java.util.stream.Collectors;

public class PostMapper {

    // Quy tắc: Chỉ map các trường @ManyToOne, không map @OneToMany
    public static Post toDomain(PostEntity entity) {
        if (entity == null) return null;
        return Post.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .price(entity.getPrice())
                .squareMeters(entity.getSquareMeters())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .user(UserMapper.toDomain(entity.getUser())) // Assumes User is also fetched or handled
                .listingType(ListingTypeMapper.toDomain(entity.getListingType())) // Assumes fetched
                .propertyType(PropertyTypeMapper.toDomain(entity.getPropertyType())) // Assumes fetched
                // --- ADD IMAGE MAPPING ---
                .images(entity.getImages() != null ? entity.getImages().stream()
                        .map(ImageMapper::toDomain) // Use your existing ImageMapper
                        .collect(Collectors.toList()) : null)
                // -------------------------
                .build();
    }

    public static PostEntity toEntity(Post model) {
        if (model == null) return null;
        PostEntity entity = new PostEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setAddress(model.getAddress());
        entity.setPrice(model.getPrice());
        entity.setSquareMeters(model.getSquareMeters());
        entity.setStatus(model.getStatus());
        entity.setCreatedAt(model.getCreatedAt());
        entity.setUpdatedAt(model.getUpdatedAt());

        // Khi lưu, chúng ta cần set các Entity quan hệ
        // (UseCase sẽ phải đảm bảo các model này có Id hợp lệ)
        if (model.getUser() != null) {
            entity.setUser(UserMapper.toEntity(model.getUser()));
        }
        if (model.getListingType() != null) {
            entity.setListingType(ListingTypeMapper.toEntity(model.getListingType()));
        }
        if (model.getPropertyType() != null) {
            entity.setPropertyType(PropertyTypeMapper.toEntity(model.getPropertyType()));
        }
        return entity;
    }
}
