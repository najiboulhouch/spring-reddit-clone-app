package net.najiboulhouch.redditcloneapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Setter @Getter
@Entity
@Table(name = "token")
public class VerificationToken extends BaseEntity{


    private String token ;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private Instant expiryDate;

}
