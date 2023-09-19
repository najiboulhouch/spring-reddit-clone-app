package net.najiboulhouch.redditcloneapp.service;

import net.najiboulhouch.redditcloneapp.exceptions.SpringRedditException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

public class CommentServiceTest {

    @Test
    @DisplayName("Test Should Pass When Comment do not Contains Swear Words")
    void shouldNotContainSwearWordsInsideComment() {
        CommentService commentService = new CommentService(null, null, null,null,null, null,null);
        //Assertions.assertFalse(commentService.containsSwearWords("This is a clean comment"));
       assertThat(commentService.containsSwearWords("This is a clean comment")).isTrue();
    }

    @Test
    @DisplayName("Should Throw Exception when Exception Contains Swear Words")
    void shouldFailWhenCommentContainsSwearWords() {
        CommentService commentService = new CommentService(null, null, null,null,null, null,null);
//        SpringRedditException exception = assertThrows(SpringRedditException.class , () -> {
//            commentService.containsSwearWords("This is shitty comment");
//        });
//        assertTrue(exception.getMessage().contains("Comments contains unacceptable language"));

        assertThatThrownBy(() -> {
            commentService.containsSwearWords("This is a shitty comment");
        }).isInstanceOf(SpringRedditException.class).hasMessage("Comments contains unacceptable language");

    }
}
