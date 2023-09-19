package net.najiboulhouch.redditcloneapp.repository;

import net.najiboulhouch.redditcloneapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTestEmbedded {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser(){
        User user = new User("test user" , "secret password" , "user@gmail.com" , true);

        User savedUser = userRepository.save(user);
        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id").isEqualTo(user);
        assertThat(savedUser.getEmail()).isEqualTo("user@gmail.com");
    }

    @Test
    @Sql("classpath:test-data.sql")
    public void shouldSavedThroughSqlFile(){
        Optional<User> test = userRepository.findByUsername("testuser_sql");
        assertThat(test).isNotEmpty();
    }
}
