package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Tag;
import io.mountblue.blogapplication.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService{
    private TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public List<String> findAllTagNames() {
        return tagRepository.getAllTagNames();
    }

    @Override
    public List<Tag> removeDuplicateTags(List<Tag> allPostTags) {
        HashSet<Tag> alreadyCreatedTags = new HashSet<>(tagRepository.findAll());
        List<Tag> newTags = new ArrayList<>();
        for(Tag tag : allPostTags){
            if(!alreadyCreatedTags.contains(tag)){
                newTags.add(tag);
            }
        }
        return newTags;
    }

    @Override
    public List<Tag> covertStringToTagType(String tagList) {
        String[] tagArray = tagList.split(",");
        List<Tag> tags = new ArrayList<>();
        HashSet<String> oldTags = new HashSet<>(tagRepository.getAllTagNames());
        for(String tagName : tagArray){
            if(!oldTags.contains(tagName.trim())){
                Tag tag = new Tag();
                tag.setName(tagName.trim());
                tags.add(tag);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findByName(String tagName) {
        return tagRepository.findByName(tagName);
    }

    @Override
    public List<Tag> findByIds(List<Long> ids) {
        return tagRepository.findByIds(ids);
    }

}
