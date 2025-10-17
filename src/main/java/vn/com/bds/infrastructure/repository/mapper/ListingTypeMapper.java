package vn.com.bds.infrastructure.repository.mapper;


import vn.com.bds.domain.model.ListingType;
import vn.com.bds.infrastructure.repository.entity.ListingTypeEntity;

public class ListingTypeMapper {

    // Quy tắc: Mapper chỉ map chính nó, không map các quan hệ @OneToMany
    public static ListingType toDomain(ListingTypeEntity entity) {
        if (entity == null) return null;
        return ListingType.builder()
                .id(entity.getId())
                .name(entity.getName())
                // Không map 'propertyTypes' ở đây để tránh vòng lặp
                .build();
    }

    public static ListingTypeEntity toEntity(ListingType model) {
        if (model == null) return null;
        ListingTypeEntity entity = new ListingTypeEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        return entity;
    }
}