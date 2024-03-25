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
    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/addComment")
    public String addComment(@ModelAttribute("comment") Comment comment,
                             @RequestParam("postId") Long postId,
                             @RequestParam("newComment") String newComment){
        if(newComment.equals("")){
            return "redirect:/post/" + postId;
        }
        if(comment.getId() != null){
            commentService.updateComment(comment, newComment);
            return "redirect:/post/" + postId;
        } else {
            commentService.saveNewComment(postId, newComment);
            return "redirect:/post/" + postId;
        }
    }

    @GetMapping("/deleteComment/{commentId}")
    public String deleteAComment(@PathVariable("commentId") Long commentId){
        Comment comment = commentService.findById(commentId);
        Post post = postService.findById(comment.getPost().getId());
        commentService.deleteComment(comment);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/updateComment/{commentId}")
    public String updateAComment(@PathVariable("commentId") Long commentId, Model model){
        Comment comment = commentService.findById(commentId);
        Post post = postService.findById(comment.getPost().getId());
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        model.addAttribute("newComment", comment.getComment());
        return "show-post";
    }
}
