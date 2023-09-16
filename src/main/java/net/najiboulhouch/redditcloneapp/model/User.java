package net.najiboulhouch.redditcloneapp.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@ToString @Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reddit_users")
public class User extends BaseEntity {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    private boolean enabled;
}
