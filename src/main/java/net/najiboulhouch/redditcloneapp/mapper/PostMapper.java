package net.najiboulhouch.redditcloneapp.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.RequiredArgsConstructor;
import net.najiboulhouch.redditcloneapp.dto.PostRequest;
import net.najiboulhouch.redditcloneapp.dto.PostResponse;
import net.najiboulhouch.redditcloneapp.model.*;
import net.najiboulhouch.redditcloneapp.repository.CommentRepository;
import net.najiboulhouch.redditcloneapp.repository.VoteRepository;
import net.najiboulhouch.redditcloneapp.service.AuthService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring" )
public abstract class PostMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mappings({
           // @Mapping(target = "createdDate" ,expression = "java(java.time.Instant.now()"),
            @Mapping(target = "description" , source = "postRequest.description"),
            @Mapping(target = "subreddit", source = "subreddit"),
            @Mapping(target = "user" ,source = "user"),
            @Mapping(target = "voteCount", constant = "0")
    })
    public abstract Post mapToEntity(PostRequest postRequest , User user ,  Subreddit subreddit );

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "subredditName", source = "subreddit.name"),
            @Mapping(target = "userName", source = "user.username"),
            @Mapping(target = "commentCount", expression = "java(commentCount(post))"),
            @Mapping(target = "postName", source = "postName"),
            @Mapping(target = "duration", expression = "java(getDuration(post))"),
            @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))"),
            @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))"),
    })
   public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    public abstract List<PostResponse> mapToListDto(List<Post> posts);

    //public abstract Page<PostResponse> mapToPageDto(List<Post> posts);

    boolean isPostUpVoted(Post post){
        return checkVoteType(post , VoteType.UPVOTE);
    }

    boolean isPostDownVoted(Post post){
        return checkVoteType(post , VoteType.DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if(authService.isLoggedIn()){
            Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByIdDesc(post , authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }

}
