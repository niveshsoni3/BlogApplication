package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username IN :selectedUsernames")
    List<User> findByUsernames(List<String> selectedUsernames);
}
