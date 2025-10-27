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

import vn.com.bds.infrastructure.security.JwtService;
import vn.com.bds.infrastructure.security.TokenBlacklistService;
import vn.com.bds.presentation.dto.ApiResponse; // <-- IMPORT MỚI
import vn.com.bds.presentation.dto.AuthResponse; // DTO (presentation)
import vn.com.bds.presentation.dto.LoginRequest;
import vn.com.bds.presentation.dto.RegisterRequest;
import vn.com.bds.presentation.dto.UserDto; // <-- IMPORT MỚI
import vn.com.bds.usecase.LoginUserUseCase;
import vn.com.bds.usecase.RegisterUserUseCase; // Interface (usecase)
import java.time.Duration; // <-- IMPORT
import java.util.Date; // <-- IMPORT


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final JwtService jwtService; // <-- INJECT JwtService
    private final TokenBlacklistService tokenBlacklistService;

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
    @PreAuthorize("isAuthenticated()") // Ensure only logged-in users can call this
    public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request, HttpServletResponse response) {

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String userEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                userEmail = jwtService.extractUsername(token); // Get user for logging
                // Extract expiration date from the token
                Date expirationDate = jwtService.extractClaim(token, claims -> claims.getExpiration()); // Use extractClaim
                Date now = new Date();

                // Calculate remaining duration (only blacklist if not already expired)
                if (expirationDate != null && expirationDate.after(now)) {
                    Duration remainingDuration = Duration.ofMillis(expirationDate.getTime() - now.getTime());
                    // Add token to blacklist using the service
                    tokenBlacklistService.addToBlacklist(token, remainingDuration);
                    log.info("User {} logged out. Token blacklisted.", userEmail);
                } else {
                    log.warn("Logout attempt with null or already expired token for user: {}", userEmail != null ? userEmail : "Unknown");
                }
            } catch (Exception e) {
                // Handle exceptions during token parsing (e.g., malformed token)
                log.error("Error processing token during logout: {}", e.getMessage());
                // Don't blacklist if token is invalid
            }
        } else {
            log.warn("Logout attempt without Bearer token.");
        }

        // --- Clear Cookies (if you use them) ---
        // Cookie jwtCookie = new Cookie("jwt-token", null);
        // jwtCookie.setPath("/");
        // jwtCookie.setMaxAge(0);
        // jwtCookie.setHttpOnly(true);
        // response.addCookie(jwtCookie);
        // log.debug("Cleared jwt-token cookie for user: {}", userEmail != null ? userEmail : "Unknown");
        // -----------------------------------

        return ApiResponse.success("Logout successful. Token invalidated."); // Update message slightly
    }
}