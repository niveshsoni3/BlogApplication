package io.mountblue.blogapplication.restcontroller;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(name = "start") Integer start,
                                                 @RequestParam(name = "limit") Integer limit,
                                                 @RequestParam(name = "sortType") String sortType){
        List<Post> posts = postService.findAll(start, limit, sortType);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId){
        Post post = postService.findById(postId);
        if(post == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<String> savePost(@RequestBody Post post,
                         @RequestParam("tagList") String tagList,
                         @RequestParam("action") boolean action,
                         @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails == null){
            return new ResponseEntity<>("Please login first", HttpStatus.BAD_GATEWAY);
        }
        postService.save(post, tagList, action);
        return new ResponseEntity<>("Post added successfully", HttpStatus.OK);

    }

    @PutMapping("/post")
    public ResponseEntity<String> updatePost(@RequestBody Post post,
                           @RequestParam("tagList") String tagList,
                           @AuthenticationPrincipal UserDetails userDetails){
        Post post1 = postService.findById(post.getId());
        if (post1 == null){
            return new ResponseEntity<>("Invalid post id", HttpStatus.BAD_REQUEST);
        } else if(postService.isValidAuthor(userDetails, post.getId())){
            postService.updatePost(post, tagList);
            new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);
    }

}
