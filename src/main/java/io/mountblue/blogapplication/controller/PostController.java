package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.service.PostService;
import io.mountblue.blogapplication.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Controller
public class PostController {
    private PostService postService;
    private TagService tagService;
    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String getAllBlog(Model model){
        List<Post> posts = postService.findAllInDesc();
        model.addAttribute("posts", posts);
        return "homepage";
    }

    @GetMapping("/newpost")
    public String showPostForm(Model model){
        Post post = new Post();
        model.addAttribute("post", post);
        return "add-post";
    }

    @PostMapping("/create")
    public String createBlogPost(@ModelAttribute("post") Post post, @RequestParam("tagList") String tagList,
                                 @RequestParam("action") boolean action){
        if(post.getId() != null){
            postService.updatePost(post, tagList);
        } else {
            postService.save(post, tagList, action);
        }
        return "redirect:/";
    }

    @GetMapping("/showPostToUpdate")
    public String updatePost(@RequestParam("postId") long postId, Model model){
        Post post = postService.findById(postId);
        model.addAttribute("post", post);
        List<Tag> tagList = post.getTags();
        String tagString = "";
        for(Tag tag : tagList){
            tagString += tag.getName() + " ,";
        }
        model.addAttribute("tagList", tagString);
        return "add-post";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("postId") long postId){
        Post post = postService.findById(postId);
        postService.removePost(post);
        return "redirect:/";
    }

    @GetMapping("/post/{postId}")
    public String readPost(@PathVariable("postId") Long id, Model model){
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("postComments", post.getComments());
        model.addAttribute("comment", new Comment());
        return "show-post";
    }

    @GetMapping("/oldest")
    public String sortByOldest(Model model){
        List<Post> sortByOldestListPosts = postService.findAllInAsc();
        model.addAttribute("posts", sortByOldestListPosts);
        return "homepage";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchString") String searchString, Model model){
        List<Post> postsBasedOnSearch = postService.searchPostsByKeyword(searchString);
        model.addAttribute("posts", postsBasedOnSearch);
        return "homepage";
    }


}
