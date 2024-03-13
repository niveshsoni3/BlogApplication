package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;

import java.util.List;

public interface PostService {
    public List<Post> findAll();
    public void save(Post post);
}
