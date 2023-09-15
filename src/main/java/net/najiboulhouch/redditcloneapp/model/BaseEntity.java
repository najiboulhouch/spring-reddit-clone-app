package net.najiboulhouch.redditcloneapp.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id ;
    protected Instant createdDate = Instant.now();
}
