package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostService {
    List<Post> findAll(Integer start, Integer limit, String sortType);
    Post save(Post post, String tagList,  boolean action);
    void saveByPost(Post post);
    Post updatePost(Post post, String tagList);
    Post findById(long id);
    void removePost(Post post);

    boolean isValidAuthor(UserDetails userDetails, Long postId);
    List<Post> searchAndFilterPostsByKeyword(String keyword, List<String> authorUsernames, String fromDateString,
                                             String toDateString, List<Long> tags, Integer start, Integer limit, String sortType);
    List<Post> searchByTitleContentTagsAndAuthorName(String searchString, String sortType, Integer start, Integer limit);


}
