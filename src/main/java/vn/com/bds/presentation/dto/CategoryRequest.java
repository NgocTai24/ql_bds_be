package vn.com.bds.presentation.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryRequest {
    private String name;
    private UUID listingTypeId; // Only used for PropertyType
}