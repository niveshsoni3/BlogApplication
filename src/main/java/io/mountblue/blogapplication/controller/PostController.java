package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String getAllBlog(Model model){
        List<Post> posts = postService.findAll();
        System.out.println(posts);
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
    public String createBlogPost(@ModelAttribute("post") Post post, @RequestParam("tagsList") String tags,
                                 @RequestParam("action") boolean action){
        System.out.println(tags);
        String[] tagArray = tags.split(",");
        List<Tag> tagList = new ArrayList<>();
        for(String tagName : tagArray){
            Tag tag = new Tag();
            tag.setName(tagName.trim());
            tagList.add(tag);
        }
        post.setTags(tagList);
        post.setCreatedAt(LocalDateTime.now());
        if (action) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());

        }
        postService.save(post);
        return "redirect:homepage";
    }



}
