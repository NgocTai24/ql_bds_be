package vn.com.bds.usecase;

import vn.com.bds.domain.model.User;

public interface GetMyProfileUseCase {
    // Input là email (sẽ lấy từ token)
    User execute(String email);
}