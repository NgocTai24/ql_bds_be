package vn.com.bds.usecase.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.bds.domain.exception.ResourceNotFoundException;
import vn.com.bds.domain.model.User;
import vn.com.bds.domain.repository.ImageStorageRepository;
import vn.com.bds.domain.repository.UserRepository;
import vn.com.bds.usecase.ManageUserUseCase; // <-- Use the single interface

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements ManageUserUseCase { // <-- Implement the single interface

    private final UserRepository userRepository;
    private final ImageStorageRepository imageStorageRepository;

    @Override
    @Transactional(readOnly = true)
    public User getMyProfile(String email) { // Renamed method
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) { // Renamed method
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() { // Renamed method
        return userRepository.findAll();
    }

    @Override
    public User updateMyProfile(UpdateMyProfileCommand command) { // Renamed method
        User userToUpdate = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + command.getEmail()));

        // Update text fields
        if (command.getFullname() != null && !command.getFullname().isBlank()) {
            userToUpdate.setFullname(command.getFullname());
        }
        if (command.getPhone() != null && !command.getPhone().isBlank()) {
            userToUpdate.setPhone(command.getPhone());
        }

        // Handle file upload
        if (command.getFileBytes() != null && command.getFileBytes().length > 0) {
            try {
                // (Optional: Add logic here to delete the old avatar from Cloudinary if userToUpdate.getAvatarUrl() is not null)
                String imageUrl = imageStorageRepository.upload(
                        command.getFileBytes(),
                        command.getFileName()
                );
                userToUpdate.setAvatarUrl(imageUrl);
            } catch (IOException e) {
                // Consider more specific exception handling or logging
                throw new RuntimeException("Failed to upload image during profile update", e);
            }
        }

        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUserById(UUID id) { // Renamed method
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
//         (Optional: Add logic here to delete the user's avatar from Cloudinary before deleting the user)
//         Optional<User> userOpt = userRepository.findById(id);
//         userOpt.ifPresent(user -> {
//             if (user.getAvatarUrl() != null) {
//                 try { imageStorageRepository.delete(user.getAvatarUrl()); } catch (Exception ignored) {}
//             }
//         });

        userRepository.deleteById(id);
    }



}