package io.mountblue.blogapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PostsTagsKey implements Serializable {
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "tag_id")
    private Long tagId;

}

