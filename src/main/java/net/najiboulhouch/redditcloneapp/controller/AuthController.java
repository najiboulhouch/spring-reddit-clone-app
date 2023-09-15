package net.najiboulhouch.redditcloneapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.najiboulhouch.redditcloneapp.dto.AuthenticationResponse;
import net.najiboulhouch.redditcloneapp.dto.LoginRequest;
import net.najiboulhouch.redditcloneapp.dto.RefreshTokenRequest;
import net.najiboulhouch.redditcloneapp.dto.RegisterRequest;
import net.najiboulhouch.redditcloneapp.service.AuthService;
import net.najiboulhouch.redditcloneapp.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest)  {
            authService.signup(registerRequest);
            return new ResponseEntity<>("User Registration Successful", HttpStatus.OK );
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully ", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
       return authService.login(loginRequest);
    }

    @PostMapping("refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> login(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest ){
        refreshTokenService.deleteByToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully !!");
    }
}
