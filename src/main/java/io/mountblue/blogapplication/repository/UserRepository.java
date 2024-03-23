package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
