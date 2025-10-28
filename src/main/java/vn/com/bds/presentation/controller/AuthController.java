package vn.com.bds.presentation.controller;

import jakarta.servlet.http.HttpServletRequest; // <-- THÊM IMPORT NÀY
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import vn.com.bds.usecase.RegisterUserUseCase;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request, HttpServletResponse response) {

        final String authHeader = request.getHeader("Authorization");
        String userEmail = "Unknown"; // Default if token parsing fails or isn't done

        // Optional: Still extract email for logging if desired, but don't *need* JwtService
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // If you still have JwtService injected for login/register, you can use it
            // try {
            //     String token = authHeader.substring(7);
            //     userEmail = jwtService.extractUsername(token);
            // } catch (Exception e) { log.warn("Could not extract username on logout."); }
        }

        log.info("User {} requested logout.", userEmail);

        // --- Clear Cookies (Keep this if you use cookies) ---
        // Cookie jwtCookie = new Cookie("jwt-token", null);
        // jwtCookie.setPath("/");
        // jwtCookie.setMaxAge(0);
        // jwtCookie.setHttpOnly(true);
        // response.addCookie(jwtCookie);
        // log.debug("Cleared jwt-token cookie for user: {}", userEmail);
        // -----------------------------------

        // Simply return success - client must discard the token
        return ApiResponse.success("Logout successful. Please discard your token.");
    }
}