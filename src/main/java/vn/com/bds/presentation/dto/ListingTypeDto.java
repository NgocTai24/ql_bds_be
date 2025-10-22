package vn.com.bds.presentation.dto;

import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.ListingType;

import java.util.UUID;

@Data
@Builder
public class ListingTypeDto {
    private UUID id;
    private String name;

    public static ListingTypeDto fromModel(ListingType model) {
        return ListingTypeDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }
}