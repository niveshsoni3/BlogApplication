package io.mountblue.blogapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.mountblue.blogapplication.restcontroller", "io.mountblue.blogapplication.service",
        "io.mountblue.blogapplication.repository", "io.mountblue.blogapplication.security", "io.mountblue.blogapplication.model"})
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
