package io.mountblue.blogapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@Embeddable
public class PostsTagsKey implements Serializable {
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "tag_id")
    private Long tagId;

}

