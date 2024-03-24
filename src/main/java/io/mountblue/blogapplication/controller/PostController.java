package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.model.User;
import io.mountblue.blogapplication.service.PostService;
import io.mountblue.blogapplication.service.TagService;
import io.mountblue.blogapplication.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PostController {
    private final PostService postService;
    private final TagService tagService;
    private final UserService userService;

    public PostController(PostService postService, TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getAllBlog(@RequestParam(name = "sortingOption", defaultValue = "newest") String sortingOption,
                             @RequestParam(value = "start", defaultValue = "0", required = false) Integer start,
                             @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                             Model model){
        List<Post> posts = postService.findAll(start, limit, sortingOption);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPageUrl", "/");
        List<Tag> allTags = tagService.findAll();
        List<User> allAuthor = userService.findAll();
        model.addAttribute("allTags", allTags );
        model.addAttribute("allAuthor", allAuthor);
        model.addAttribute("selectedTags", new ArrayList<Tag>());
        model.addAttribute("selectedAuthors", new ArrayList<User>());
        model.addAttribute("start", start);
        model.addAttribute("limit", limit);
        return "homepage";
    }

    @GetMapping("/newPost")
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
        StringBuilder tagString = new StringBuilder();
        for(Tag tag : tagList){
            tagString.append(tag.getName()).append(" ,");
        }
        model.addAttribute("tagList", tagString.toString());
        return "add-post";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("postId") long postId){
        Post post = postService.findById(postId);
        postService.removePost(post);
        return "redirect:/";
    }

    @GetMapping("/post/{postId}")
    public String readPost(@PathVariable("postId") Long id, Model model,
                           @AuthenticationPrincipal UserDetails userDetails){
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("postComments", post.getComments());
        model.addAttribute("comment", new Comment());
        if(userDetails != null){
            model.addAttribute("currentUser", userDetails.getUsername());
        }
        return "show-post";
    }

    @GetMapping("/search")
    public String filterPosts(@RequestParam(value = "sortingOption", defaultValue = "newest") String sortingOption,
                          @RequestParam(value = "start", defaultValue = "0", required = false) Integer start,
                          @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                          @RequestParam("searchString") String searchString,
                          Model model){
        List<Post> posts = postService.searchByTitleContentTagsAndAuthorName(searchString, sortingOption, start, limit);
        List<Tag> allTags = tagService.findAll();
        List<User> allAuthor = userService.findAll();
        model.addAttribute("allTags", allTags );
        model.addAttribute("allAuthor", allAuthor);
        model.addAttribute("posts", posts);
        model.addAttribute("start", start);
        model.addAttribute("limit", limit);
        model.addAttribute("searchString", searchString);
        model.addAttribute("selectedTags", new HashSet<Tag>());
        model.addAttribute("currentPageUrl", "/search");
        return "homepage";
    }

    @GetMapping("/filters")
    public String searchPosts(@RequestParam(value = "sortingOption", defaultValue = "newest") String sortingOption,
                         @RequestParam("searchString") String searchString,
                         @RequestParam(name = "selectedAuthors", required = false) List<String> selectedAuthors,
                         @RequestParam(name = "publishedFrom", required = false) String publishedFrom,
                         @RequestParam(name = "publishedTo", required = false) String publishedTo,
                         @RequestParam(name = "selectedTags" , required = false) List<Long>  selectedTags,
                         @RequestParam(value = "start", defaultValue = "0", required = false) Integer start,
                         @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                         Model model){
        System.out.println("up");
        List<Tag> selectedTagsObject = tagService.findByIds(selectedTags);
        List<User> selectedAuthorsObject = userService.findByUsernames(selectedAuthors);
        System.out.println("here");
        List<Post> postsBasedOnSearch = postService.searchAndFilterPostsByKeyword(searchString, selectedAuthors,
        publishedFrom, publishedTo, selectedTags, start, limit, sortingOption);
        System.out.println("here2");
        model.addAttribute("posts", postsBasedOnSearch);
        List<Tag> allTags = tagService.findAll();
        List<User> allAuthor = userService.findAll();
        model.addAttribute("allTags", allTags );
        model.addAttribute("allAuthor", allAuthor);
        model.addAttribute("selectedTags", selectedTags == null? new ArrayList<Tag>() : selectedTagsObject);
        System.out.println("here3");
        model.addAttribute("selectedAuthors", selectedAuthors == null? new ArrayList<User>() : selectedAuthorsObject);
        System.out.println("here4");
        model.addAttribute("start", start);
        model.addAttribute("limit", limit);
        model.addAttribute("searchString", searchString);
        model.addAttribute("currentPageUrl", "/filters");
        System.out.println("down");
        return "homepage";
    }
}
