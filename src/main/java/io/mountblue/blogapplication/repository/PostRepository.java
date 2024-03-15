package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByPublishedAtAsc();
    List<Post> findAllByOrderByPublishedAtDesc();

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN FETCH p.author a " +
            "LEFT JOIN FETCH p.tags t " +
            "WHERE p.title LIKE CONCAT('%', :keyword, '%') " +
            "   OR p.content LIKE CONCAT('%', :keyword, '%') " +
            "   OR a.name LIKE CONCAT('%', :keyword, '%') " +
            "   OR t.name LIKE CONCAT('%', :keyword, '%')")
    List<Post> searchPostsByKeyword(String keyword);
}
