package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.User;

import java.util.List;

public interface UserService {
    boolean isUserExist(String username);
    void saveUser(User user);
    List<User> findAll();
    List<User> findByUsernames(List<String> selectedUsernames);
    User findByUsername(String username);
}
