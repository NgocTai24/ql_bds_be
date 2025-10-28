package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.presentation.dto.AuthResponse;


public interface ManageAuthUseCase {

    /**
     * Registers a new user using email/password.
     * @param command Registration details.
     * @return AuthResponse containing token and user info.
     */
    AuthResponse register(RegisterUserCommand command);

    /**
     * Logs in a user using email/password.
     * @param command Login credentials.
     * @return AuthResponse containing token and user info.
     */
    AuthResponse login(LoginUserCommand command);

    /**
     * Logs in or registers a user based on Google OAuth2 information.
     * @param command User details from Google.
     * @return AuthResponse containing token and user info.
     */
    AuthResponse loginWithGoogle(GoogleLoginCommand command);

    // --- Nested Command Classes ---

    @Value @Builder
    class RegisterUserCommand {
        String fullname;
        String email;
        String password;
        String phone;
    }

    @Value @Builder
    class LoginUserCommand {
        String email;
        String password;
    }

    @Value @Builder
    class GoogleLoginCommand {
        String email;
        String fullname;
        String avatarUrl;
    }
}