package vn.com.bds.presentation.dto;

import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.Image;
import java.util.UUID;

@Data
@Builder
public class ImageDto {
    private UUID id;
    private String imageUrl;

    public static ImageDto fromModel(Image model) {
        if(model == null) return null;
        return ImageDto.builder()
                .id(model.getId())
                .imageUrl(model.getImageUrl())
                .build();
    }
}