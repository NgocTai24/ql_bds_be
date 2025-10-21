package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;

public interface LoginWithGoogleUseCase {

    // UseCase này sẽ nhận thông tin từ Google và trả về
    // AuthResponse (giống hệt như RegisterUserUseCase)
    RegisterUserUseCase.AuthResponse execute(GoogleLoginCommand command);

    @Value
    @Builder
    class GoogleLoginCommand {
        String email;
        String fullname;
        String avatarUrl;
    }
}