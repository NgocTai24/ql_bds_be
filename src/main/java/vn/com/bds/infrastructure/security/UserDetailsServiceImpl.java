package vn.com.bds.infrastructure.security;

import lombok.RequiredArgsConstructor;
// Import UserDetails của Spring Security
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
// Import "cổng" UserRepository từ DOMAIN
import vn.com.bds.domain.repository.UserRepository;

import java.util.Collections;

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

        // Chuyển đổi Model (domain) sang UserDetails (Spring Security)
        return new User(
                userModel.getEmail(),
                userModel.getPassword(),
                Collections.emptyList() // Tạm thời chưa dùng Role
        );
    }
}