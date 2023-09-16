package net.najiboulhouch.redditcloneapp.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.najiboulhouch.redditcloneapp.config.AppConfig;
import net.najiboulhouch.redditcloneapp.dto.AuthenticationResponse;
import net.najiboulhouch.redditcloneapp.dto.LoginRequest;
import net.najiboulhouch.redditcloneapp.dto.RefreshTokenRequest;
import net.najiboulhouch.redditcloneapp.dto.RegisterRequest;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceNotFoundException;
import net.najiboulhouch.redditcloneapp.exceptions.SpringRedditException;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceFoundException;
import net.najiboulhouch.redditcloneapp.model.NotificationEmail;
import net.najiboulhouch.redditcloneapp.model.User;
import net.najiboulhouch.redditcloneapp.model.VerificationToken;
import net.najiboulhouch.redditcloneapp.repository.UserRepository;
import net.najiboulhouch.redditcloneapp.repository.VerificationTokenRepository;
import net.najiboulhouch.redditcloneapp.security.JwtProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private final JwtProvider jwtProvider ;
    private final RefreshTokenService refreshTokenService;
    private final AppConfig appConfig;

    public void signup(RegisterRequest registerRequest){

        userRepository.findByEmailOrUsername(registerRequest.getEmail() , registerRequest.getUsername()).ifPresent(s -> {
            throw new ResourceFoundException("User found with email " + registerRequest.getEmail() + " or name " + registerRequest.getUsername());
        });

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        try {
            CompletableFuture<String> stringCompletableFuture = mailService.sendMail(new NotificationEmail("Please Activate your Account",
                    user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                    "please click on the below url to activate your account : " +
                    appConfig.getUrl() + "/api/auth/accountVerification/" + token));
            log.info(stringCompletableFuture.get());
        } catch (SpringRedditException | InterruptedException | ExecutionException e) {
            throw new SpringRedditException(e.getMessage());
        }

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnabled(verificationToken.get());
    }

    private void fetchUserAndEnabled(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
       User user =  userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with name " + username));
       user.setEnabled(true);
       userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername() ,loginRequest.getPassword() ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
       String token = jwtProvider.generateToken(authentication);
       return AuthenticationResponse.builder()
               .authenticationToken(token)
               .refreshToken(refreshTokenService.generateRefreshToken().getToken())
               .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
               .username(loginRequest.getUsername())
               .build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
