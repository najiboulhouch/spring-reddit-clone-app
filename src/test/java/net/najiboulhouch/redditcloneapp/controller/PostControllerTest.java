package net.najiboulhouch.redditcloneapp.controller;

import net.najiboulhouch.redditcloneapp.dto.PostResponse;
import net.najiboulhouch.redditcloneapp.security.JwtProvider;
import net.najiboulhouch.redditcloneapp.service.PostService;
import net.najiboulhouch.redditcloneapp.service.UserDetailsServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @MockBean
    private PostService postService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JwtProvider jwtProvider;
    @Autowired
    private MockMvc mockMvc;



    @Test
    @DisplayName("Should List All Posts When making GET request to endpoint - /api/posts")
    void shouldListAllPosts() throws Exception {
        PostResponse postResponse1 = new PostResponse(1L , "Post Name" , "http://local.site" , "Description" ,
                "User1" , "Subreddit Name" , 0 , 0, "1 day ago" , false , false);

        PostResponse postResponse2 = new PostResponse(1L , "Post Name" , "http://local.site" , "Description" ,
                "User1" , "Subreddit Name" , 0 , 0, "1 day ago" , false , false);

        Pageable pageable = new PageRequest(1 , 2);

        List<PostResponse> postResponses = Arrays.asList(postResponse1, postResponse2);
        Page<PostResponse> pageResponse = new PageImpl<>(postResponses);

        Mockito.when(postService.getAllPosts(pageable)).thenReturn(pageResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @DisplayName("Should Get Post By ID")
    void shouldReturnPost() throws Exception {
        PostResponse postResponse = new PostResponse(1L , "Post Name" , "http://local.site" , "Description" ,
                "User1" , "Subreddit Name" , 0 , 0, "1 day ago" , false , false);

        Mockito.when(postService.getPost(1L)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{id}" , 1L))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id" , Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postName" , Matchers.is("Post Name")));
    }
}
