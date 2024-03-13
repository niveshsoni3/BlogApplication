package io.mountblue.blogapplication.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String excerpt;
    private String content;
    private String author;
    private LocalDateTime publishedAt;
    private boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
