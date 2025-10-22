package vn.com.bds.presentation.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID; // <-- Import

@Data
public class CreatePostRequest {
    private String title;
    private String description;
    private String address;
    private BigDecimal price; // Use BigDecimal
    private Double squareMeters;
    private Long userId;
    private UUID listingTypeId; // <-- Change to UUID
    private UUID propertyTypeId; // <-- Change to UUID
}