package io.mountblue.blogapplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @NotNull(message = "username can't be null")
    private String username;
    @NotNull(message = "email can't be null")
    private String email;
    @NotNull(message = "password can't be null")
    private String password;
    private boolean enabled;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Role> roles;

    @Override
    public String toString() {
        return username;
    }
}
