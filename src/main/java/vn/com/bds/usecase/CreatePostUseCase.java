package vn.com.bds.usecase;

import vn.com.bds.domain.model.Post;

// Dùng tên cụ thể cho từng nghiệp vụ
public interface CreatePostUseCase {
    Post execute(CreatePostCommand command);

    // Dùng một lớp "Command" để đóng gói dữ liệu đầu vào
    @lombok.Value // Tạo class immutable (chỉ getter, all-args constructor)
    @lombok.Builder
    class CreatePostCommand {
        String title;
        String description;
        String address;
        java.math.BigDecimal price;
        Double squareMeters;
        Long userId;
        Integer listingTypeId;
        Integer propertyTypeId;
    }
}
