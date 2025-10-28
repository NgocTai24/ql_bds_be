package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType; // Ensure this is imported
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Ensure this is imported
import vn.com.bds.domain.model.User;
import vn.com.bds.presentation.dto.ApiResponse;
import vn.com.bds.presentation.dto.UserDto;
import vn.com.bds.usecase.ManageUserUseCase; // <-- Use the single interface

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    // INJECT THE SINGLE USE CASE INTERFACE
    private final ManageUserUseCase manageUserUseCase;

    /**
     * API 1: Get current user's profile
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // CALL RENAMED METHOD
        User userModel = manageUserUseCase.getMyProfile(userDetails.getUsername());
        UserDto userDto = UserDto.fromModel(userModel);
        return ApiResponse.success(userDto, "Get my profile successfully");
    }

    /**
     * API 2: Get any user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        // CALL RENAMED METHOD
        User userModel = manageUserUseCase.getUserById(id);
        UserDto userDto = UserDto.fromModel(userModel);
        return ApiResponse.success(userDto, "Get user successfully");
    }

    /**
     * API 3: Get all users (Admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        // CALL RENAMED METHOD
        List<User> userModels = manageUserUseCase.getAllUsers();
        List<UserDto> userDtos = userModels.stream()
                .map(UserDto::fromModel)
                .collect(Collectors.toList());
        return ApiResponse.success(userDtos, "Get all users successfully");
    }


    /**
     * API 4: Update current user's profile (form-data)
     */
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserDto>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "phone", required = false) String phone,
            // Changed parameter name for clarity, still links to 'file' key in form-data
            @RequestParam(value = "file", required = false) MultipartFile avatarFile
    ) throws IOException {

        byte[] fileBytes = null;
        String fileName = null;
        if (avatarFile != null && !avatarFile.isEmpty()) {
            fileBytes = avatarFile.getBytes();
            fileName = avatarFile.getOriginalFilename();
        }

        // Use the nested Command from ManageUserUseCase
        ManageUserUseCase.UpdateMyProfileCommand command = ManageUserUseCase.UpdateMyProfileCommand.builder()
                .email(userDetails.getUsername())
                .fullname(fullname)
                .phone(phone)
                .fileBytes(fileBytes)
                .fileName(fileName)
                .build();

        // CALL RENAMED METHOD
        User updatedUser = manageUserUseCase.updateMyProfile(command);
        UserDto userDto = UserDto.fromModel(updatedUser);
        return ApiResponse.success(userDto, "Profile updated successfully");
    }

    /**
     * API 5: Delete a user by ID (Admin only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        // CALL RENAMED METHOD
        manageUserUseCase.deleteUserById(id);
        return ApiResponse.success("User deleted successfully");
    }
}