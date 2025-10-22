package vn.com.bds.infrastructure.security;

import lombok.RequiredArgsConstructor;
// Import UserDetails của Spring Security
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
// Import "cổng" UserRepository từ DOMAIN
import vn.com.bds.domain.repository.UserRepository;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inject "cổng" (interface) từ domain, KHÔNG inject adapter
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Chúng ta dùng email làm "username" để đăng nhập
        vn.com.bds.domain.model.User userModel = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // --- SỬA LẠI CHỖ NÀY ---
        // Tạo một danh sách quyền (Authority) từ Role của user
        // Spring Security yêu cầu Role phải có tiền tố "ROLE_"
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + userModel.getRole().name())
        );

        return new org.springframework.security.core.userdetails.User(
                userModel.getEmail(),
                userModel.getPassword(),
                authorities // <-- TRUYỀN DANH SÁCH QUYỀN VÀO ĐÂY
        );
    }
}