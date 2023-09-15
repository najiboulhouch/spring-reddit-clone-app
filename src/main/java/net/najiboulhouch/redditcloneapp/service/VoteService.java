package net.najiboulhouch.redditcloneapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.najiboulhouch.redditcloneapp.dto.PostRequest;
import net.najiboulhouch.redditcloneapp.dto.PostResponse;
import net.najiboulhouch.redditcloneapp.dto.VoteDto;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceNotFoundException;
import net.najiboulhouch.redditcloneapp.exceptions.SpringRedditException;
import net.najiboulhouch.redditcloneapp.mapper.PostMapper;
import net.najiboulhouch.redditcloneapp.mapper.VoteMapper;
import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.Subreddit;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.model.Vote;
import net.najiboulhouch.redditcloneapp.repository.PostRepository;
import net.najiboulhouch.redditcloneapp.repository.SubredditRepository;
import net.najiboulhouch.redditcloneapp.repository.UserRepository;
import net.najiboulhouch.redditcloneapp.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static net.najiboulhouch.redditcloneapp.model.VoteType.UPVOTE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;
    public void vote(VoteDto voteDto){

        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(
                 () -> new ResourceNotFoundException("Post Not Found with ID " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByIdDesc(post , authService.getCurrentUser() );

        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }

        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount() + 1);
        }else{
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(voteMapper.mapToVote(voteDto , post , authService.getCurrentUser()));
        postRepository.save(post);
    }




}
