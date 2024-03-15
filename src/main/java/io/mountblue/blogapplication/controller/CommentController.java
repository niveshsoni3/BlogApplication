package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.service.CommentService;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {
    private CommentService commentService;
    private PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/addComment")
    public String addComment(@ModelAttribute("comment") Comment comment,
                             @RequestParam("postId") long postId,
                             @RequestParam("newComment") String newComment){
        System.out.println(comment.getComment() + " comment value");
        if(comment.getId() != null){
            Comment comment1 = commentService.findById(comment.getId());
            comment1.setComment(newComment);
            commentService.save(comment1);
            return "redirect:/post/" + postId;
        } else {
            Post post = postService.findById(postId);
            List<Comment> postComments = post.getComments();
            Comment comment1 = new Comment();
            comment1.setPost(post);
            comment1.setComment(newComment);
            postComments.add(comment1);
            post.setComments(postComments);
            postService.saveByPost(post);
            return "redirect:/post/" + postId;
        }
    }

    @GetMapping("/deleteComment/{commentId}")
    public String deleteAComment(@PathVariable("commentId") long commentId){
        Comment comment = commentService.findById(commentId);
        Post post = postService.findById(comment.getPost().getId());
        commentService.deleteComment(comment);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/updateComment/{commentId}")
    public String updateAComment(@PathVariable("commentId") long commentId, Model model){
        Comment comment = commentService.findById(commentId);
        Post post = postService.findById(comment.getPost().getId());
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        model.addAttribute("newComment", comment.getComment());
        System.out.println(comment.getComment());
        return "show-post";
    }
}
