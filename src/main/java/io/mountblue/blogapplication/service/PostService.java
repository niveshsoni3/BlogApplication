package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;

import java.util.List;

public interface PostService {
    public List<Post> findAllInAsc();
    public List<Post> findAllInDesc();
    public void save(Post post, String tagList,  boolean action);
    public Post findById(long id);
    public void removePost(Post post);
    public void updatePost(Post post, String tagList);
    public void saveByPost(Post post);
    public List<Post> searchPostsByKeywordInDesc(String keyword);
    public List<Post> searchPostsByKeywordInAsc(String keyword);
}
