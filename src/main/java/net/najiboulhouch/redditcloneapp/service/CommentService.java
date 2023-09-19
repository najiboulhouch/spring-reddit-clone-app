package net.najiboulhouch.redditcloneapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.najiboulhouch.redditcloneapp.dto.CommentsDto;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceNotFoundException;
import net.najiboulhouch.redditcloneapp.exceptions.SpringRedditException;
import net.najiboulhouch.redditcloneapp.mapper.CommentMapper;
import net.najiboulhouch.redditcloneapp.model.Comment;
import net.najiboulhouch.redditcloneapp.model.NotificationEmail;
import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.repository.CommentRepository;
import net.najiboulhouch.redditcloneapp.repository.PostRepository;
import net.najiboulhouch.redditcloneapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private static final String POST_URL = "";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService ;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder ;
    private final MailService mailService ;
    private final UserRepository userRepository;



    public void save(CommentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id : " + commentsDto.getPostId()));
        User user = authService.getCurrentUser();
        Comment comment = commentMapper.mapToEntity(commentsDto , user , post);
        commentRepository.save(comment);

        String message = mailContentBuilder.buildEmail(post.getUser().getUsername() + " posted a comment on your post. " + POST_URL);
        sendCommentNotification(message , post.getUser());
    }


    private void sendCommentNotification(String message , User user){
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Comment on your post" , user.getEmail(),  message));
    }

    @Transactional(readOnly = true)
    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id : " + postId));

        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new ResourceNotFoundException(userName));

        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new SpringRedditException("Comments contains unacceptable language");
        }
        return true;
    }
}
