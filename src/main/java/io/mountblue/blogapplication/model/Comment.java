package io.mountblue.blogapplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String comment;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.PERSIST})
    @JoinColumn(name = "post_id")
    private Post post;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
