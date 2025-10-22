package vn.com.bds.usecase;

import vn.com.bds.domain.model.User;
import java.util.List;

public interface GetAllUsersUseCase {
    List<User> getAllUser();
}