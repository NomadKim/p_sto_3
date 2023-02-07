package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends ReadWriteDao<User, Long>{
    Optional<User> getUserByEmail(String username);
    List<User> getUsersByIds(List<Long> ids);
    void updatePasswordByEmail(String email, String password);

    void disableUserByEmail(String email);

    List<User> getAllByRole(Role role);
}
