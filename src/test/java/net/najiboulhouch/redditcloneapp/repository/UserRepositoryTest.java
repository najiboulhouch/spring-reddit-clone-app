package net.najiboulhouch.redditcloneapp.repository;

import net.najiboulhouch.redditcloneapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUser(){
        User expectedUserObject = new User("test user" , "secret password" , "user@gmail.com" , true);
        User actualUserObject   = userRepository.save(expectedUserObject);
        assertThat(actualUserObject).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedUserObject);
        assertThat(actualUserObject.getEmail()).isEqualTo("user@gmail.com");
    }
}
