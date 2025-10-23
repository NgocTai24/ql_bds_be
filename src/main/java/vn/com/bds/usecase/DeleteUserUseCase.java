package vn.com.bds.usecase;

import java.util.UUID;

public interface DeleteUserUseCase {
    // Input là ID của user cần xóa
    void deleteById(UUID id);
}