package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Comment;

public interface CommentService {
    public Comment findById(long id);

    public void deleteComment(Comment comment);
}
