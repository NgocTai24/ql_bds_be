package vn.com.bds.usecase;

import lombok.Builder;
import lombok.Value;
import vn.com.bds.domain.model.User;


public interface UpdateMyProfileUseCase {

    User execute(UpdateMyProfileCommand command);

    @Value
    @Builder
    class UpdateMyProfileCommand {
        String email;
        String fullname;
        String phone;

        // --- THÊM 2 TRƯỜNG NÀY ---
        // Chúng ta sẽ để 2 trường này là null nếu user không upload file
        byte[] fileBytes;
        String fileName;
    }
}