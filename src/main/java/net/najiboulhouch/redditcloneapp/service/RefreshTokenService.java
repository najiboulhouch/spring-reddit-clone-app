package net.najiboulhouch.redditcloneapp.service;

import lombok.RequiredArgsConstructor;
import net.najiboulhouch.redditcloneapp.exceptions.SpringRedditException;
import net.najiboulhouch.redditcloneapp.model.RefreshToken;
import net.najiboulhouch.redditcloneapp.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }

    public void deleteByToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
