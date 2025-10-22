package vn.com.bds.usecase;

public interface DeleteUserUseCase {
    // Input là ID của user cần xóa
    void deleteById(Long id);
}