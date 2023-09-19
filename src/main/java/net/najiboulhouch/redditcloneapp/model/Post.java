package net.najiboulhouch.redditcloneapp.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Setter @Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Post extends BaseEntity {

    @NotBlank(message = "Post Name cannot by empty or Null")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    @Lob
    private String description;
    private Integer voteCount = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subreddit_Id" , referencedColumnName = "id")
    private Subreddit subreddit;

    public Post(Long id, Instant createdDate, String postName, @Nullable String url, @Nullable String description, Integer voteCount, User user, Subreddit subreddit) {
        super(id, createdDate);
        this.postName = postName;
        this.url = url;
        this.description = description;
        this.voteCount = voteCount;
        this.user = user;
        this.subreddit = subreddit;
    }
}
