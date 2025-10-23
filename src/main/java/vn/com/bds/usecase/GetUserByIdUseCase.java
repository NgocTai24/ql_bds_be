package vn.com.bds.usecase;

import vn.com.bds.domain.model.User;

import java.util.UUID;

public interface GetUserByIdUseCase {
    User execute(UUID id);
}