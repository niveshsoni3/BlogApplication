package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{
    private PostRepository postRepository;
    private TagService tagService;

    public PostServiceImpl(PostRepository postRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.tagService = tagService;
    }

    @Override
    public List<Post> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts;
    }

    @Override
    public void save(Post post, String tagList, boolean action) {
        List<Tag> tags = tagService.covertStringToTagType(tagList);
        post.setTags(tags);
        post.setCreatedAt(LocalDateTime.now());
        if (action) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());
        }
        postRepository.save(post);
    }

    @Override
    public Post findById(long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.get();
    }

    @Override
    public void removePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public void updatePost(Post post, String tagList) {
        Post existingPost = postRepository.findById(post.getId()).get();
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        String[] updatedTagNames = tagList.split(",");
        Collection<Tag> existingTags = existingPost.getTags();
        Map<String, Tag> existingTagsByName = new HashMap<>();
        for (Tag tag : existingTags) {
            existingTagsByName.put(tag.getName(), tag);
        }
        List<Tag> updatedTags = new ArrayList<>();
        for (String tagName : updatedTagNames) {
            Tag tag = existingTagsByName.get(tagName);
            if (tag == null) {
                tag = new Tag(tagName);
            }
            updatedTags.add(tag);
        }
        Set<Tag> tagsToAdd = new HashSet<>(updatedTags);
        Set<Tag> tagsToRemove = new HashSet<>(existingTags);
        tagsToRemove.removeAll(updatedTags);
        existingPost.getTags().addAll(tagsToAdd);
        existingPost.getTags().removeAll(tagsToRemove);
        existingPost.setTags(updatedTags);
        postRepository.save(existingPost);
    }
}
