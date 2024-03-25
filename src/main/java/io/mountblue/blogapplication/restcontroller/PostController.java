package io.mountblue.blogapplication.restcontroller;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts(@RequestParam(name = "start") Integer start,
                                  @RequestParam(name = "limit") Integer limit,
                                  @RequestParam(name = "sortType") String sortType){
        return postService.findAll(start, limit, sortType);
    }

    @GetMapping("/post/{postId}")
    public Post getPost(@PathVariable Long postId){
        return postService.findById(postId);
    }

    @PostMapping("/post")
    public Post savePost(@RequestBody Post post,
                         @RequestParam("tagList") String tagList,
                         @RequestParam("action") boolean action){
        return postService.save(post, tagList, action);

    }

}