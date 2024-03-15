package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Tag;

import java.util.List;

public interface TagService {
    public List<String> findAllTagNames();
    public List<Tag> removeDuplicateTags(List<Tag> tags);

    List<Tag> covertStringToTagType(String tagList);

    List<Tag> findAll();

    Tag findByName(String tagName);
}
