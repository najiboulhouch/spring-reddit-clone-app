package net.najiboulhouch.redditcloneapp.repository;

import net.najiboulhouch.redditcloneapp.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class postRepositoryTest {


    @Autowired
    private PostRepository postRepository;

    @Test
    public void shouldSavePost(){
        Post exceptedPostObject = new Post(123L, Instant.now() ,"First Post" , "https://url.site", "Test" , 0 , null  , null);
        Post actualPost = postRepository.save(exceptedPostObject);
        assertThat(actualPost).usingRecursiveComparison().ignoringFields("id").isEqualTo(exceptedPostObject);
    }
}
