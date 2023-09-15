package net.najiboulhouch.redditcloneapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    private String token;
}
