package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.domain.model.User;

public interface LoginUserUseCase {

    // Sử dụng lại AuthTokenResponse từ RegisterUserUseCase
    RegisterUserUseCase.AuthResponse execute(LoginUserCommand command);

    @Value
    @Builder
    class LoginUserCommand {
        String email;
        String password;
    }
}