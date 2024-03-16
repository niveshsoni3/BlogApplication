package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;
    private PostService postService;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
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
    public void updateComment(Comment comment,  String newComment) {
        comment = commentRepository.findById(comment.getId()).get();
        comment.setComment(newComment);
        commentRepository.save(comment);
    }

    @Override
    public void saveNewComment(long postId, String newComment) {
        Post post = postService.findById(postId);
        List<Comment> postComments = post.getComments();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setComment(newComment);
        postComments.add(comment);
        post.setComments(postComments);
        postService.saveByPost(post);
    }
}
