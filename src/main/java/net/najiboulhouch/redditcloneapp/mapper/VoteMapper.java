package net.najiboulhouch.redditcloneapp.mapper;

import net.najiboulhouch.redditcloneapp.dto.PostResponse;
import net.najiboulhouch.redditcloneapp.dto.VoteDto;
import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring" )
public interface VoteMapper {

    @Mappings({
            @Mapping(target = "post", source = "post"),
            @Mapping(target = "user", source = "user")
    })
    Vote mapToVote(VoteDto voteDto , Post post , User user);
}
