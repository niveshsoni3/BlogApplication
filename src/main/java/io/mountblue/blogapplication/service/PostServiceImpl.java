package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Post;
import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.model.User;
import io.mountblue.blogapplication.repository.PostRepository;
import io.mountblue.blogapplication.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final TagService tagService;

    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, TagService tagService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @Override
    public List<Post> findAll(Integer start, Integer limit, String sortType) {
        System.out.println("here");
        if(sortType.equals("oldest")){
            Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").ascending());
            Page<Post> postPage = postRepository.findAll(pageable);
            return postPage.getContent();
        }
        Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").descending());
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.getContent();
    }

    @Override
    public void save(Post post, String tagList, boolean action) {
        String[] newTagNames = tagList.trim().split(",");
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
            post.setExcerpt(post.getContent().substring(0, 149).concat("..."));
        } else {
            post.setExcerpt(post.getContent());
        }
        post.setTags(updatedTags);
        post.setCreatedAt(LocalDateTime.now());
        if (action) {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User author = userRepository.findByUsername(username).get();

        post.setAuthor(author);
        postRepository.save(post);
    }

    @Override
    public void updatePost(Post post, String tagList) {
        Post existingPost = postRepository.findById(post.getId()).get();
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());
        String[] updatedTagNames = tagList.trim().split(",");
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
    public Post findById(long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.get();
    }

    @Override
    public void removePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public void saveByPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public List<Post> searchAndFilterPostsByKeyword(String keyword, List<String> authorUsernames, String fromDateString,
                                                    String toDateString, List<Long> tags, Integer start,
                                                    Integer limit, String sortType) {
        LocalDateTime fromDateTime = dateFormatter(fromDateString);
        LocalDateTime toDateTime = dateFormatter(toDateString);
        List<Post> searchedPost  = new ArrayList<>();
        if (!keyword.isEmpty()) {
            if (sortType.equals("oldest")) {
                Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").ascending());
                Page<Post> postPage = postRepository.searchPostsByKeywordOrderByPublished(keyword, pageable);
                searchedPost.addAll(postPage.getContent());
            } else {
                Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").descending());
                Page<Post> postPage = postRepository.searchPostsByKeywordOrderByPublished(keyword, pageable);
                searchedPost.addAll(postPage.getContent());
            }
        } else {
            if(sortType.equals("oldest")){
                Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").ascending());
                Page<Post> postPage = postRepository.filterByAuthorsDateAndTags(authorUsernames, fromDateTime, toDateTime, tags, pageable);
                return postPage.getContent();
            } else {
                Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").descending());
                Page<Post> postPage = postRepository.filterByAuthorsDateAndTags(authorUsernames, fromDateTime, toDateTime, tags, pageable);
                return postPage.getContent();
            }
        }
        Set<Post> allFilteredPost = postRepository.filterByAuthorsDateAndTagsWithoutPagination(authorUsernames, fromDateTime, toDateTime, tags);
        List<Post> allFilteredAndSearchedPosts = new ArrayList<>();
        for(Post post : searchedPost){
            if (allFilteredPost.contains(post)){
                allFilteredAndSearchedPosts.add(post);
            }
        }
        return allFilteredAndSearchedPosts;
    }

    @Override
    public List<Post> searchByTitleContentTagsAndAuthorName(String searchString, String sortType, Integer start, Integer limit) {
        if(searchString.isEmpty()){
            return findAll(start, limit, sortType);
        }
        if(sortType.equals("oldest")){
            Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").ascending());
            Page<Post> postPage = postRepository.searchPostsByKeywordOrderByPublished(searchString, pageable);
            return postPage.getContent();
        }
        Pageable pageable = PageRequest.of(start, limit, Sort.by("publishedAt").descending());
        Page<Post> postPage = postRepository.searchPostsByKeywordOrderByPublished(searchString, pageable);
        return postPage.getContent();
    }

    public static LocalDateTime dateFormatter(String dateString){
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);
        LocalTime time = LocalTime.parse("00:00:00", timeFormatter);
        return date.atTime(time);
    }
}
