package vn.com.bds.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.bds.domain.model.Role;
import vn.com.bds.domain.model.User;
import vn.com.bds.domain.repository.UserRepository;
import vn.com.bds.infrastructure.security.JwtService;
import vn.com.bds.usecase.LoginUserUseCase;
import vn.com.bds.usecase.RegisterUserUseCase;
import vn.com.bds.domain.exception.ResourceNotFoundException;
import vn.com.bds.usecase.LoginWithGoogleUseCase; // <-- IMPORT MỚI
import java.util.Optional;

@Lazy
@Service // Báo cho Spring biết đây là Bean
@RequiredArgsConstructor
@Transactional // Đảm bảo các thao tác DB được an toàn
public class AuthServiceImpl implements RegisterUserUseCase, LoginUserUseCase, LoginWithGoogleUseCase {

    // --- Inject các "cổng" (domain) và "dịch vụ" (infrastructure) ---
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService; // Sẽ inject UserDetailsServiceImpl

    @Override
    public AuthResponse execute(RegisterUserCommand command) {
        userRepository.findByEmail(command.getEmail()).ifPresent(user -> {
            // Dùng Exception tùy chỉnh của chúng ta
            throw new RuntimeException("Email already exists");
        });

        // ... code mã hóa password ...

        User user = User.builder()
                .fullname(command.getFullname())
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .phone(command.getPhone())
                .role(Role.USER)
                .build();

        // 1. Lưu user và LẤY LẠI user đã lưu (có ID)
        User savedUser = userRepository.save(user);

        // 2. Tạo token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtService.generateToken(userDetails);

        // 3. Trả về AuthResponse mới (chứa cả token và user model)
        return AuthResponse.builder()
                .token(token)
                .user(savedUser) // <-- TRẢ VỀ USER
                .build();
    }

    @Override
    public AuthResponse execute(LoginUserCommand command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.getEmail(),
                        command.getPassword()
                )
        );

        // 1. LẤY User model từ DB (để có đầy đủ thông tin)
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found after authentication")); // Dùng exception mới

        // 2. Tạo UserDetails (Spring Security cần)
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // 3. Tạo token
        String token = jwtService.generateToken(userDetails);

        // 4. Trả về AuthResponse mới (chứa cả token và user model)
        return AuthResponse.builder()
                .token(token)
                .user(user) // <-- TRẢ VỀ USER
                .build();
    }

    // --- TRIỂN KHAI HÀM MỚI ---
    @Override
    public AuthResponse execute(LoginWithGoogleUseCase.GoogleLoginCommand command) {

        // 1. Kiểm tra xem user đã tồn tại trong DB của mình chưa
        Optional<User> userOptional = userRepository.findByEmail(command.getEmail());

        User user;
        if (userOptional.isPresent()) {
            // Nếu CÓ: Chỉ cần lấy ra
            user = userOptional.get();
            // (Bạn có thể thêm logic cập nhật avatar/tên nếu muốn)
        } else {
            // Nếu CHƯA: Tạo user mới
            user = User.builder()
                    .fullname(command.getFullname())
                    .email(command.getEmail())
                    // Mật khẩu có thể để null hoặc một chuỗi ngẫu nhiên
                    // vì họ sẽ không bao giờ dùng nó để đăng nhập
                    .password(passwordEncoder.encode(java.util.UUID.randomUUID().toString()))
                    .avatarUrl(command.getAvatarUrl())
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        // 2. Tạo UserDetails cho Spring Security
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // 3. Tạo JWT (token của chính bạn)
        String token = jwtService.generateToken(userDetails);

        // 4. Trả về AuthResponse
        return AuthResponse.builder()
                .token(token)
                .user(user)
                .build();
    }
}