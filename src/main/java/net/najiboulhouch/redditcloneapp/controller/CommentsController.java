package net.najiboulhouch.redditcloneapp.controller;

import lombok.RequiredArgsConstructor;
import net.najiboulhouch.redditcloneapp.dto.CommentsDto;
import net.najiboulhouch.redditcloneapp.service.CommentService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComments(@RequestBody CommentsDto commentRequest){
        commentService.save(commentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(params = "postId")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPosts(@RequestParam("postId") Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByUser(name));
    }
}
