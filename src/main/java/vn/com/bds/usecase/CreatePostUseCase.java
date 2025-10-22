package vn.com.bds.usecase;

import vn.com.bds.domain.model.Post;
import java.util.UUID; // <-- Import
import java.math.BigDecimal; // <-- Import if missing

public interface CreatePostUseCase {
    Post execute(CreatePostCommand command);

    @lombok.Value
    @lombok.Builder
    class CreatePostCommand {
        String title;
        String description;
        String address;
        BigDecimal price; // Use BigDecimal for price
        Double squareMeters;
        Long userId; // User ID remains Long
        UUID listingTypeId; // <-- Change to UUID
        UUID propertyTypeId; // <-- Change to UUID
    }
}