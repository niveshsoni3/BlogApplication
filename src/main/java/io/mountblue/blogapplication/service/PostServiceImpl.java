package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.model.User;
import io.mountblue.blogapplication.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    public List<Post> findAllInAsc() {
        List<Post> posts = postRepository.findAllByOrderByPublishedAtAsc();
        return posts;
    }

    @Override
    public List<Post> findAllInDesc() {
        List<Post> posts = postRepository.findAllByOrderByPublishedAtDesc();
        return posts;
    }

    @Override
    public void save(Post post, String tagList, boolean action) {
        String[] newTagNames = tagList.split(",");
        List<Tag> existingTags = tagService.findAll();
        Map<String, Tag> allTagsByName = new HashMap<>();
        for (Tag tag : existingTags) {
            allTagsByName.put(tag.getName(), tag);
        }
        List<Tag> updatedTags = new ArrayList<>();
        for (String tagName : newTagNames) {
            Tag tag = allTagsByName.get(tagName.trim());
            if (tag == null) {
                tag = new Tag(tagName.trim());
            }
            updatedTags.add(tag);
        }
        if(post.getContent().length() > 150){
            post.setExcerpt(post.getContent().substring(0, 149));
        } else {
            post.setExcerpt(post.getContent());
        }
        post.setTags(updatedTags);
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
        existingPost.setUpdatedAt(LocalDateTime.now());
        String[] updatedTagNames = tagList.split(",");
        List<Tag> existingTags = tagService.findAll();
        Map<String, Tag> existingTagsByName = new HashMap<>();
        for (Tag tag : existingTags) {
            existingTagsByName.put(tag.getName(), tag);
        }
        List<Tag> updatedTags = new ArrayList<>();
        for (String tagName : updatedTagNames) {
            Tag tag = existingTagsByName.get(tagName.trim());
            if (tag == null) {
                tag = new Tag(tagName.trim());
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

    @Override
    public void saveByPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public List<Post> searchPostsByKeywordInDesc(String keyword) {
        return postRepository.searchPostsByKeywordOrderByPublishedAtDesc(keyword);
    }

    @Override
    public List<Post> searchPostsByKeywordInAsc(String keyword) {
        return postRepository.searchPostsByKeywordOrderByPublishedAtAsc(keyword);
    }

    @Override
    public List<Post> findByAuthorsDateAndTags(List<User> authorIds, String fromDateString, String toDateString, List<Long> tags) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateFromDate = LocalDate.parse(fromDateString, formatter);
        LocalDate dateToDate = LocalDate.parse(toDateString, formatter);
        String fromTimeString = "00:00:00";
        String toTimeString = "23:59:59";
        LocalTime fromParsedTime = LocalTime.parse(fromTimeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime toParsedTime = LocalTime.parse(toTimeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalDateTime fromDate = LocalDateTime.of(dateFromDate, fromParsedTime);
        LocalDateTime toDate = LocalDateTime.of(dateToDate, toParsedTime);
        return postRepository.findByAuthorsDateAndTags(authorIds,fromDate,toDate,tags);
    }
}
