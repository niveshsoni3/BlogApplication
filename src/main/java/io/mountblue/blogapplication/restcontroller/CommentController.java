package io.mountblue.blogapplication.restcontroller;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.service.CommentService;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;

    public CommentController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<Comment>> getAllCommentsByPost(@PathVariable Long postId,
                                                              @AuthenticationPrincipal UserDetails userDetails){
        Post post = postService.findById(postId);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else if (postService.isValidAuthor(userDetails, postId)) {
            List<Comment> comments = post.getComments();
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("comment/{postId}")
    public ResponseEntity<String> addComment(@PathVariable Long postId,
                                             @RequestParam("newComment") String newComment,
                                             @AuthenticationPrincipal UserDetails userDetails){
        Post post = postService.findById(postId);
        if(post == null){
            return new ResponseEntity<>("Invalid post id", HttpStatus.BAD_REQUEST);
        } else if (postService.isValidAuthor(userDetails, postId)) {
            commentService.saveNewComment(postId, newComment);
            return new ResponseEntity<>("Comment added successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Access denied ", HttpStatus.UNAUTHORIZED);
    }

}
