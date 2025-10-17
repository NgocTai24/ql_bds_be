package vn.com.bds.domain.model;

public enum PostStatus {
    DRAFT,          // Bản nháp, chỉ người đăng thấy
    PENDING_APPROVAL, // Chờ duyệt
    PUBLISHED,      // Đã duyệt, hiển thị công khai
    REJECTED,       // Bị từ chối
    EXPIRED,        // Hết hạn
    SOLD            // Đã bán/Đã cho thuê
}