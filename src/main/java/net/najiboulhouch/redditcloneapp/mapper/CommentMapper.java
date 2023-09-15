package net.najiboulhouch.redditcloneapp.mapper;

import net.najiboulhouch.redditcloneapp.dto.CommentsDto;
import net.najiboulhouch.redditcloneapp.dto.PostRequest;
import net.najiboulhouch.redditcloneapp.dto.PostResponse;
import net.najiboulhouch.redditcloneapp.model.Comment;
import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.Subreddit;
import net.najiboulhouch.redditcloneapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mappings({
            @Mapping(target = "text", source = "commentsDto.text"),
            @Mapping(target = "user", source = "user"),
            @Mapping(target = "post", source = "post"),
    })
    Comment mapToEntity(CommentsDto commentsDto , User user , Post post );

    @Mappings({
            @Mapping(target = "postId", expression = "java(comment.getPost().getId())"),
            @Mapping(target = "username", expression  = "java(comment.getUser().getUsername())")
    })
    CommentsDto mapToDto(Comment comment);

}
