package vn.com.bds.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.bds.domain.exception.ResourceNotFoundException;
import vn.com.bds.domain.model.User;
import vn.com.bds.domain.repository.ImageStorageRepository;
import vn.com.bds.domain.repository.UserRepository;
import vn.com.bds.usecase.*;

import java.io.IOException;
import java.util.List;

@Service // Báo cho Spring biết đây là Bean
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements GetMyProfileUseCase, GetUserByIdUseCase, UpdateMyProfileUseCase, GetAllUsersUseCase, DeleteUserUseCase {

    private final UserRepository userRepository;
    private final ImageStorageRepository imageStorageRepository;


    // 1. Triển khai GetMyProfile
    @Override
    @Transactional(readOnly = true) // Chỉ đọc, không thay đổi DB
    public User execute(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    // 2. Triển khai GetUserById
    @Override
    @Transactional(readOnly = true)
    public User execute(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // 3. Triển khai UpdateMyProfile
    @Override
    public User execute(UpdateMyProfileCommand command) {
        User userToUpdate = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + command.getEmail()));

        // Cập nhật text
        if (command.getFullname() != null && !command.getFullname().isBlank()) {
            userToUpdate.setFullname(command.getFullname());
        }
        if (command.getPhone() != null && !command.getPhone().isBlank()) {
            userToUpdate.setPhone(command.getPhone());
        }

        // CẬP NHẬT LOGIC UPLOAD
        if (command.getFileBytes() != null && command.getFileBytes().length > 0) {
            try {
                String imageUrl = imageStorageRepository.upload(
                        command.getFileBytes(), // <-- Dùng byte array
                        command.getFileName()
                );
                userToUpdate.setAvatarUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image during profile update", e);
            }
        }

        return userRepository.save(userToUpdate);
    }

    // --- TRIỂN KHAI HÀM DELETE ---
    @Override
    public void deleteById(Long id) {
        // 1. Kiểm tra xem user có tồn tại không
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        // (Trong thực tế, bạn có thể cần kiểm tra xem user này có quyền xóa không,
        // hoặc không cho phép xóa chính mình, hoặc xóa user admin...)

        // 2. Gọi repository để xóa
        userRepository.deleteById(id);
        // (Bạn cũng có thể cần xóa ảnh của user trên Cloudinary ở đây)
    }

    @Override
    @Transactional(readOnly = true) // Read-only operation
    public List<User> getAllUser() {
        return userRepository.findAll(); // Assuming your UserRepository has findAll()
    }
}