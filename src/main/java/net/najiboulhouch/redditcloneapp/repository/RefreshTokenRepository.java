package net.najiboulhouch.redditcloneapp.repository;

import net.najiboulhouch.redditcloneapp.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByToken(String token);
    Optional<RefreshToken> findByToken(String token);
}
