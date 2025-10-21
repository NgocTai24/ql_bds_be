package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.domain.model.User;

public interface RegisterUserUseCase {

    // UseCase sẽ trả về một đối tượng Response
    AuthResponse execute(RegisterUserCommand command);

    // Dùng "Command" để đóng gói dữ liệu đầu vào
    @Value // Tạo class immutable
    @Builder
    class RegisterUserCommand {
        String fullname;
        String email;
        String password;
        String phone;
    }

    // Dùng "Response" để đóng gói dữ liệu đầu ra
    @Value // Tạo class immutable
    @Builder
    class AuthResponse {
        String token;
        User user;
    }
}