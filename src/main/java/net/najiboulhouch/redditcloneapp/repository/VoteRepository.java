package net.najiboulhouch.redditcloneapp.repository;


import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.model.Vote;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends BaseRepository<Vote> {

    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post , User currentUser);
}
