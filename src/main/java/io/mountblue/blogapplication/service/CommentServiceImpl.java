package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findById(long id) {
        Comment comment = commentRepository.findById(id).get();
        return comment;
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
