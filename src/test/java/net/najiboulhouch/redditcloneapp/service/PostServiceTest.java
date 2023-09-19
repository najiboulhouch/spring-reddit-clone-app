package net.najiboulhouch.redditcloneapp.service;

import net.najiboulhouch.redditcloneapp.dto.PostRequest;
import net.najiboulhouch.redditcloneapp.dto.PostResponse;
import net.najiboulhouch.redditcloneapp.mapper.PostMapper;
import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.Subreddit;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.repository.PostRepository;
import net.najiboulhouch.redditcloneapp.repository.SubredditRepository;
import net.najiboulhouch.redditcloneapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.emptyList;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock PostRepository postRepository;
    @Mock SubredditRepository subredditRepository;
    @Mock UserRepository userRepository;
    @Mock AuthService authService ;
    @Mock PostMapper postMapper ;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;
    private PostService postService;

    @BeforeEach
    public void setUp(){
         postService = new PostService(postRepository,authService, subredditRepository, userRepository,postMapper);
    }

    @Test
    @DisplayName("Should Find Post By Id")
    void shouldFindPostById() {
        Post post = new Post(123L, Instant.now() ,"First Post" , "https://url.site", "Test" , 0 , null  , null);
        PostResponse expectedPostResponse = new PostResponse(123L , "First Post" , "https://url.site", "Test" , "Test User" ,"Test Subreddit" ,
                0 , 0 , "1 Hour Ago" , false , false);
        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

        PostResponse actualPostResponse = postService.getPost(123L);
        assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("Should Save Posts")
    void shouldSavePosts() {
        User currentUser = new User("test user" , "secret password" , "user@gmail.com" , true);
        currentUser.setId(123L);
        currentUser.setCreatedDate(Instant.now());

        Subreddit subreddit = new Subreddit("First Subreddit" , "Subreddit Description" , emptyList() , currentUser);
        subreddit.setId(123L);
        subreddit.setCreatedDate(Instant.now());

        Post post = new Post(123L, Instant.now() ,"First Post" , "https://url.site", "Test" , 0 , null  , null);

        PostRequest postRequest = new PostRequest(null , "First Subreddit" , "First Post" , "https://url.site" , "Test");

        Mockito.when(subredditRepository.findByName("First Subreddit")).thenReturn(Optional.of(subreddit));
        Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);
        Mockito.when(postMapper.mapToEntity(postRequest , currentUser , subreddit)).thenReturn(post);
        postService.save(postRequest);
        Mockito.verify(postRepository , Mockito.times(1)).save(postArgumentCaptor.capture());

        assertThat(postArgumentCaptor.getValue().getId()).isEqualTo(123L);
        assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");



    }
}
