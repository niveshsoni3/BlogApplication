package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.User;
import io.mountblue.blogapplication.repository.CommentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostService postService;

    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
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
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public void saveNewComment(long postId, String newComment) {
        Post post = postService.findById(postId);
        List<Comment> postComments = post.getComments();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setComment(newComment);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User author = userService.findByUsername(username);
        comment.setUser(author);
        postComments.add(comment);
        post.setComments(postComments);
        postService.saveByPost(post);
    }
}
