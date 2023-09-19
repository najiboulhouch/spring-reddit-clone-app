package net.najiboulhouch.redditcloneapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import net.najiboulhouch.redditcloneapp.config.AppConfig;
import net.najiboulhouch.redditcloneapp.dto.RegisterRequest;
import net.najiboulhouch.redditcloneapp.model.NotificationEmail;
import net.najiboulhouch.redditcloneapp.model.Post;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.model.VerificationToken;
import net.najiboulhouch.redditcloneapp.repository.UserRepository;
import net.najiboulhouch.redditcloneapp.repository.VerificationTokenRepository;
import net.najiboulhouch.redditcloneapp.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import  org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private  PasswordEncoder passwordEncoder;
    @Mock private  UserRepository userRepository;
    @Mock private  VerificationTokenRepository verificationTokenRepository;
    @Mock private  AuthenticationManager authenticationManager;
    @Mock private  MailService mailService;
    @Mock private  JwtProvider jwtProvider ;
    @Mock private  RefreshTokenService refreshTokenService;
    @Mock private  AppConfig appConfig;
    private AuthService authService;

    @Captor
    private ArgumentCaptor<User> postArgumentCaptor;

    @BeforeEach
    public void setUp(){
        authService = new AuthService(passwordEncoder,userRepository,verificationTokenRepository,authenticationManager,mailService,jwtProvider,refreshTokenService,appConfig);
    }


    @Test
    @DisplayName("Should Sign Up")
    void shouldSignup() {
        User user = new User("test user" , "secret password" , "user@gmail.com" , true);
        user.setId(123L);
        user.setCreatedDate(Instant.now());
        RegisterRequest registerRequest = new RegisterRequest("najib@gmail.com" , "najib" ,passwordEncoder.encode("123"));

        Mockito.when(userRepository.findByEmailOrUsername("najib@gmail.com" , "najib")).thenReturn(Optional.empty());
        Mockito.when(mailService.sendMail(Mockito.any(NotificationEmail.class))).thenReturn(CompletableFuture.completedFuture("Send a mail to user" + user.getEmail()));

        authService.signup(registerRequest);
        Mockito.verify(userRepository , Mockito.times(1)).save(postArgumentCaptor.capture());

        assertThat(postArgumentCaptor.getValue().getUsername()).isEqualTo("najib");

    }

    @Test
    @DisplayName("Should Verify An Account")
    void shouldVerifyAccount() {
        User user = new User("test user" , "secret password" , "user@gmail.com" , true);
        VerificationToken verificationToken = new VerificationToken("token" ,user , Instant.now().plusMillis(500000));

        Mockito.when(verificationTokenRepository.findByToken("token")).thenReturn(Optional.of(verificationToken));
        Mockito.when(userRepository.findByUsername("test user")).thenReturn(Optional.of(user));

        authService.verifyAccount("token");
        Mockito.verify(userRepository , Mockito.times(1)).save(postArgumentCaptor.capture());
        assertThat(postArgumentCaptor.getValue().isEnabled()).isEqualTo(true);
    }
}
