package vn.com.bds.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.bds.presentation.dto.ApiResponse; // <-- IMPORT MỚI
import vn.com.bds.presentation.dto.AuthResponse; // DTO (presentation)
import vn.com.bds.presentation.dto.LoginRequest;
import vn.com.bds.presentation.dto.RegisterRequest;
import vn.com.bds.presentation.dto.UserDto; // <-- IMPORT MỚI
import vn.com.bds.usecase.LoginUserUseCase;
import vn.com.bds.usecase.RegisterUserUseCase; // Interface (usecase)

@RestController
@RequestMapping("/auth") // URL của bạn là /auth/register và /auth/login
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {

        RegisterUserUseCase.RegisterUserCommand command = RegisterUserUseCase.RegisterUserCommand.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .build();

        // 1. Gọi UseCase, nhận về AuthResponse (domain)
        RegisterUserUseCase.AuthResponse useCaseResponse = registerUserUseCase.execute(command);

        // 2. Chuyển đổi sang AuthResponse (presentation DTO)
        AuthResponse responseDto = AuthResponse.builder()
                .token(useCaseResponse.getToken())
                .user(UserDto.fromModel(useCaseResponse.getUser())) // Map sang DTO
                .build();

        // 3. Trả về bằng ApiResponse (wrapper)
        return ApiResponse.created(responseDto, "User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {

        LoginUserUseCase.LoginUserCommand command = LoginUserUseCase.LoginUserCommand.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        // 1. Gọi UseCase, nhận về AuthResponse (domain)
        RegisterUserUseCase.AuthResponse useCaseResponse = loginUserUseCase.execute(command);

        // 2. Chuyển đổi sang AuthResponse (presentation DTO)
        AuthResponse responseDto = AuthResponse.builder()
                .token(useCaseResponse.getToken())
                .user(UserDto.fromModel(useCaseResponse.getUser())) // Map sang DTO
                .build();

        // 3. Trả về bằng ApiResponse (wrapper)
        return ApiResponse.success(responseDto, "Login successful");
    }
}