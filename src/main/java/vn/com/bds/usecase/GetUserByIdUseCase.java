package vn.com.bds.usecase;

import vn.com.bds.domain.model.User;

public interface GetUserByIdUseCase {
    User execute(Long id);
}