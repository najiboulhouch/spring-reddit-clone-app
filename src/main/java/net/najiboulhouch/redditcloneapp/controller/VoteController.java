package net.najiboulhouch.redditcloneapp.controller;

import lombok.RequiredArgsConstructor;
import net.najiboulhouch.redditcloneapp.dto.CommentsDto;
import net.najiboulhouch.redditcloneapp.dto.VoteDto;
import net.najiboulhouch.redditcloneapp.model.Vote;
import net.najiboulhouch.redditcloneapp.service.CommentService;
import net.najiboulhouch.redditcloneapp.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes/")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto){
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
