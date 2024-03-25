package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Role;
import io.mountblue.blogapplication.model.User;
import io.mountblue.blogapplication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isUserExist(String username) {
        Optional<User> user =  userRepository.findByUsername(username);
        System.out.println(user.isPresent());
        return user.isPresent();
    }

    @Override
    public void saveUser(User user) {
        Role role = new Role();
        role.setRole("ROLE_AUTHOR");
        role.setUser(user);
        user.setEnabled(true);
        user.setRoles(new ArrayList<>(List.of(role)));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByUsernames(List<String> selectedUsernames) {
        return userRepository.findByUsernames(selectedUsernames);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
}
