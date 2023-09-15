package net.najiboulhouch.redditcloneapp.repository;

import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.Subreddit;
import net.najiboulhouch.redditcloneapp.model.User;


import java.util.List;


public interface PostRepository extends BaseRepository<Post> {
    List<Post> findBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}
