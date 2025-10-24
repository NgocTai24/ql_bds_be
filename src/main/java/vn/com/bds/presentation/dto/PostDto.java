package vn.com.bds.presentation.dto;

import lombok.Builder;
import lombok.Data;
import vn.com.bds.domain.model.Post;
import vn.com.bds.domain.model.PostStatus; // Import enum if needed

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class PostDto {
    private UUID id;
    private String title;
    private String description;
    private String address;
    private BigDecimal price;
    private Double squareMeters;
    private String status; // Send status name as string
    private Instant createdAt;
    private Instant updatedAt;
    private UserDto user; // Embed User DTO
    private ListingTypeDto listingType; // Embed ListingType DTO
    private PropertyTypeDto propertyType; // Embed PropertyType DTO
    private List<ImageDto> images; // Embed Image DTO list

    public static PostDto fromModel(Post model) {
        if (model == null) return null;
        return PostDto.builder()
                .id(model.getId())
                .title(model.getTitle())
                .description(model.getDescription())
                .address(model.getAddress())
                .price(model.getPrice())
                .squareMeters(model.getSquareMeters())
                .status(model.getStatus() != null ? model.getStatus().name() : null)
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .user(UserDto.fromModel(model.getUser()))
                .listingType(ListingTypeDto.fromModel(model.getListingType()))
                .propertyType(PropertyTypeDto.fromModel(model.getPropertyType()))
                .images(model.getImages() != null ? model.getImages().stream()
                        .map(ImageDto::fromModel)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}