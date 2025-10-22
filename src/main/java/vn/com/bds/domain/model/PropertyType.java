package vn.com.bds.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

// Đây là "Loại BĐS" (ví dụ: Căn hộ, Nhà riêng, Đất nền)
@Data
@Builder
public class PropertyType {
    private UUID id;
    private String name; // "Căn hộ chung cư"

    // Mỗi loại BĐS thuộc về 1 loại đăng tin (Căn hộ -> Bán)
    private ListingType listingType;
}