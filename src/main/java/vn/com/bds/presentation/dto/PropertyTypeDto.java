package vn.com.bds.presentation.dto;

import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.PropertyType;

import java.util.UUID;

@Data
@Builder
public class PropertyTypeDto {
    private UUID id;
    private String name;
    private UUID listingTypeId; // Include parent ID

    public static PropertyTypeDto fromModel(PropertyType model) {
        return PropertyTypeDto.builder()
                .id(model.getId())
                .name(model.getName())
                .listingTypeId(model.getListingType() != null ? model.getListingType().getId() : null)
                .build();
    }
}