package io.mountblue.blogapplication.repository;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT DISTINCT p " +
            "FROM Post p " +
            "LEFT JOIN p.author a " +
            "LEFT JOIN p.tags t " +
            "WHERE (:keyword IS NULL OR " +
            "p.title LIKE %:keyword% OR " +
            "p.content LIKE %:keyword% OR " +
            "a.username LIKE %:keyword% OR " +
            "t.name LIKE %:keyword%)")
    Page<Post> searchPostsByKeywordOrderByPublished(String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.author a " +
            "WHERE (:authorUsernames IS NULL OR a.username IN :authorUsernames) " +
            "AND (:fromDate IS NULL OR :toDate IS NULL OR p.publishedAt BETWEEN :fromDate AND :toDate) " +
            "AND (:tags IS NULL OR t.id IN (:tags)) " +
            "GROUP BY p.id")
    Page<Post> filterByAuthorsDateAndTags( List<String> authorUsernames,
                                         LocalDateTime fromDate,
                                         LocalDateTime toDate,
                                         List<Long> tags, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.author a " +
            "WHERE (:authorUsernames IS NULL OR a.username IN :authorUsernames) " +
            "AND (:fromDate IS NULL OR :toDate IS NULL OR p.publishedAt BETWEEN :fromDate AND :toDate) " +
            "AND (:tags IS NULL OR t.id IN (:tags)) " +
            "GROUP BY p.id")
    Set<Post> filterByAuthorsDateAndTagsWithoutPagination( List<String> authorUsernames,
                                           LocalDateTime fromDate,
                                           LocalDateTime toDate,
                                           List<Long> tags);
}
