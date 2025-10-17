package vn.com.bds.presentation.dto;


import lombok.Data;

// DTO này ánh xạ với JSON request từ client (Postman)
@Data
public class CreatePostRequest {
    private String title;
    private String description;
    private String address;
    private java.math.BigDecimal price;
    private Double squareMeters;
    private Long userId; // Tạm thời truyền userId, sau này sẽ lấy từ Token
    private Integer listingTypeId;
    private Integer propertyTypeId;
}