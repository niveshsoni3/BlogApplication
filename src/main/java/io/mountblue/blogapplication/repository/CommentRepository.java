package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
