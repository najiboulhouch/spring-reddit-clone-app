package net.najiboulhouch.redditcloneapp.repository;

import net.najiboulhouch.redditcloneapp.model.VerificationToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VerificationTokenRepository extends BaseRepository<VerificationToken> {
    Optional<VerificationToken> findByToken(String token);


}
