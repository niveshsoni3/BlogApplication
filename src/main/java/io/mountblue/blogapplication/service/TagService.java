package io.mountblue.blogapplication.service;

import io.mountblue.blogapplication.model.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {
    public List<String> findAllTagNames();
    public List<Tag> removeDuplicateTags(List<Tag> tags);

    List<Tag> covertStringToTagType(String tagList);

    List<Tag> findAll();

    Tag findByName(String tagName);

    List<Tag> findByIds(List<Long> ids);
}
