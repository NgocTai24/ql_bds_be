package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

// Đây là "Loại đăng tin" (ví dụ: Bán, Cho thuê)
@Data
@Builder
public class ListingType {
    private UUID id; // <-- Sửa thành UUID
    private String name; // "Bán" hoặc "Cho thuê"

    // Một loại đăng tin (Bán) có nhiều loại BĐS (Đất, Nhà)
    private List<PropertyType> propertyTypes;
}