package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends ReadWriteService<User,Long>{
    void updatePasswordByEmail(String email, String password);

    void disableUserByEmail(String email);

    List<User> getAllByRole(Role role);

    List<User> getUsersByIds(List<Long> ids);
}
