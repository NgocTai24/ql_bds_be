package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.com.bds.domain.model.User;
import vn.com.bds.presentation.dto.ApiResponse;
import vn.com.bds.presentation.dto.UserDto;
import vn.com.bds.usecase.*;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users") // Tiền tố chung cho User
@RequiredArgsConstructor
public class UserController {

    // Inject các "cổng" UseCase
    private final GetMyProfileUseCase getMyProfileUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateMyProfileUseCase updateMyProfileUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;


    /**
     * API 1: Lấy thông tin của chính user đang đăng nhập
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getMyProfile(
            // @AuthenticationPrincipal: Tự động lấy thông tin user đã đăng nhập từ Token
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // userDetails.getUsername() chính là email của user
        User userModel = getMyProfileUseCase.execute(userDetails.getUsername());
        UserDto userDto = UserDto.fromModel(userModel);
        return ApiResponse.success(userDto, "Get my profile successfully");
    }

    /**
     * API 2: Lấy thông tin CÔNG KHAI của 1 user bất kỳ bằng ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID id) {
        User userModel = getUserByIdUseCase.execute(id);
        UserDto userDto = UserDto.fromModel(userModel);
        return ApiResponse.success(userDto, "Get user successfully");
    }

    /**
     * API 3: Cập nhật thông tin của chính user đang đăng nhập
     */
    // Sửa lại annotation @PutMapping
    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserDto>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "avatarUrl", required = false) MultipartFile file
    ) throws IOException {

        // THAY ĐỔI CÁCH LẤY DỮ LIỆU FILE
        // InputStream fileStream = null; // Xóa
        byte[] fileBytes = null; // Thêm
        String fileName = null;
        if (file != null && !file.isEmpty()) {
            // fileStream = file.getInputStream(); // Xóa
            fileBytes = file.getBytes(); // Thay bằng dòng này
            fileName = file.getOriginalFilename();
        }

        // CẬP NHẬT COMMAND BUILDER
        UpdateMyProfileUseCase.UpdateMyProfileCommand command = UpdateMyProfileUseCase.UpdateMyProfileCommand.builder()
                .email(userDetails.getUsername())
                .fullname(fullname)
                .phone(phone)
                // .fileStream(fileStream) // Xóa
                .fileBytes(fileBytes)   // Thêm
                .fileName(fileName)
                .build();

        User updatedUser = updateMyProfileUseCase.execute(command);
        UserDto userDto = UserDto.fromModel(updatedUser);
        return ApiResponse.success(userDto, "Profile updated successfully");
    }

    @DeleteMapping("/{id}")
    // Chỉ cho phép user có quyền 'ADMIN' gọi API này
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        deleteUserUseCase.deleteById(id);
        // Trả về 200 OK với message (hoặc 204 No Content nếu muốn)
        return ApiResponse.success("User deleted successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        // 1. Call Use Case
        List<User> userModels = getAllUsersUseCase.getAllUser();

        // 2. Map List<User> to List<UserDto>
        List<UserDto> userDtos = userModels.stream()
                .map(UserDto::fromModel) // Use the existing mapper
                .collect(Collectors.toList());

        // 3. Return ApiResponse
        return ApiResponse.success(userDtos, "Get all users successfully");
    }

}