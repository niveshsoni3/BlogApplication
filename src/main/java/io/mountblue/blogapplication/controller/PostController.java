package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.model.Comment;
import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.model.User;
import io.mountblue.blogapplication.service.PostService;
import io.mountblue.blogapplication.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class PostController {
    private PostService postService;
    private TagService tagService;
    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String getAllBlog(@RequestParam(name = "sortingOption", defaultValue = "newest") String sortingOption, Model model){
        if(sortingOption.equals("oldest")){
            List<Post> posts = postService.findAllInAsc();
            model.addAttribute("posts", posts);
        } else {
            List<Post> posts = postService.findAllInDesc();
            model.addAttribute("posts", posts);
        }
        model.addAttribute("currentPageUrl", "/");
        List<Tag> allTags = tagService.findAll();
        model.addAttribute("allTags", allTags );
        model.addAttribute("selectedTags", new ArrayList<Tag>());
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

    @GetMapping("/search")
    public String search(@RequestParam("sortingOption") String sortingOption,
                         @RequestParam("searchString") String searchString,
                         Model model){
        if(sortingOption.equals("newest")){
            List<Post> postsBasedOnSearch = postService.searchPostsByKeywordInDesc(searchString);
            model.addAttribute("posts", postsBasedOnSearch);
            model.addAttribute("searchString", searchString);
        } else {
            List<Post> postsBasedOnSearch = postService.searchPostsByKeywordInAsc(searchString);
            model.addAttribute("posts", postsBasedOnSearch);
            model.addAttribute("searchString", searchString);
        }
        model.addAttribute("currentPageUrl", "/search");

        return "homepage";
    }

    @GetMapping("/filters")
    public String filters(@RequestParam(name = "selectedAuthor", required = false) List<User> selectedAuthors,
                          @RequestParam(name = "publishedFrom", required = false) String publishedFrom,
                          @RequestParam(name = "publishedTo", required = false) String publishedTo,
                          @RequestParam(name = "selectedTags" , required = false) List<Long>  selectedTags,
                          Model model){
        Set<Tag> selectedTagsObject = tagService.findByIds(selectedTags);
        List<Post> posts = postService.findByAuthorsDateAndTags( selectedAuthors,publishedFrom, publishedTo, selectedTags);
        List<Tag> allTags = tagService.findAll();
        model.addAttribute("allTags", allTags );
        model.addAttribute("selectedTags", selectedTagsObject);
        model.addAttribute("posts", posts);
        model.addAttribute("publishedFrom", publishedFrom);
        model.addAttribute("publishedTo", publishedTo);
        return "homepage";
    }

    @GetMapping("/pagination")
    public String pagination(@RequestParam(value = "start", defaultValue = "0", required = false) Integer start,
                             @RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                             Model model){
        System.out.println(start + " " + limit);
        List<Post> posts = postService.findPostsWithPagination(start, limit);
        System.out.println(posts.size());
        model.addAttribute("posts", posts);
        model.addAttribute("start", start);
        model.addAttribute("limit", limit);
        return "pagination";
    }
}
