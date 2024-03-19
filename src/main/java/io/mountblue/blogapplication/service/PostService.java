package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    List<Post> findAllInAsc();
    List<Post> findAllInDesc();
    void save(Post post, String tagList,  boolean action);
    Post findById(long id);
    void removePost(Post post);
    void updatePost(Post post, String tagList);
    void saveByPost(Post post);
    List<Post> searchPostsByKeywordInDesc(String keyword);
    List<Post> searchPostsByKeywordInAsc(String keyword);
    List<Post> findByAuthorsDateAndTags(List<User> authorIds, String fromDate, String toDate, List<Long> tags);
    List<Post> findPostsWithPagination(Integer start, Integer limit);


}
