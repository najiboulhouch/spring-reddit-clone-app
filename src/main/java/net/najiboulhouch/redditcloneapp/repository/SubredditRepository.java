package net.najiboulhouch.redditcloneapp.repository;

import net.najiboulhouch.redditcloneapp.model.Subreddit;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends BaseRepository<Subreddit> {
    Optional<Subreddit> findByName(String subredditName);

}
