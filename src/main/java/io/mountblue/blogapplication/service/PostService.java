package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostService {
    List<Post> findAll(Integer start, Integer limit, String sortType);
    void save(Post post, String tagList,  boolean action);
    void saveByPost(Post post);
    void updatePost(Post post, String tagList);
    Post findById(long id);
    void removePost(Post post);
    List<Post> searchAndFilterPostsByKeyword(String keyword, List<User> authorIds, String fromDateString,
                                             String toDateString, List<Long> tags, Integer start, Integer limit, String sortType);
    List<Post> searchByTitleContentTagsAndAuthorName(String searchString, String sortType, Integer start, Integer limit);


}
