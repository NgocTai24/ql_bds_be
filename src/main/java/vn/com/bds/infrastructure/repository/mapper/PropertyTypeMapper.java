package vn.com.bds.infrastructure.repository.mapper;

import vn.com.bds.domain.model.PropertyType;
import vn.com.bds.infrastructure.repository.entity.PropertyTypeEntity;

public class PropertyTypeMapper {

    public static PropertyType toDomain(PropertyTypeEntity entity) {
        if (entity == null) return null;
        return PropertyType.builder()
                .id(entity.getId())
                .name(entity.getName())
                // Map quan hệ @ManyToOne (listingType)
                .listingType(ListingTypeMapper.toDomain(entity.getListingType()))
                .build();
    }

    public static PropertyTypeEntity toEntity(PropertyType model) {
        if (model == null) return null;
        PropertyTypeEntity entity = new PropertyTypeEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        // Map quan hệ @ManyToOne (listingType)
        if (model.getListingType() != null) {
            entity.setListingType(ListingTypeMapper.toEntity(model.getListingType()));
        }
        return entity;
    }
}