package net.najiboulhouch.redditcloneapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.najiboulhouch.redditcloneapp.dto.PostRequest;
import net.najiboulhouch.redditcloneapp.dto.PostResponse;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceNotFoundException;
import net.najiboulhouch.redditcloneapp.mapper.PostMapper;
import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.Subreddit;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.repository.PostRepository;
import net.najiboulhouch.redditcloneapp.repository.SubredditRepository;
import net.najiboulhouch.redditcloneapp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final AuthService authService ;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;

    private final PostMapper postMapper;

    public void save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new ResourceNotFoundException("Subreddit not found with name : " + postRequest.getSubredditName()));

        User currentUser = authService.getCurrentUser();

        Post post = postMapper.mapToEntity(postRequest ,  currentUser , subreddit);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id : " + id));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(final Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId){
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new ResourceNotFoundException("Subreddit not found with id : " + subredditId));
        List<Post> posts = postRepository.findBySubreddit(subreddit);
        return postMapper.mapToListDto(posts);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username : " + username));
        return postMapper.mapToListDto(postRepository.findByUser(user));
    }



}
