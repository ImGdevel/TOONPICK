package toonpick.app.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.entity.Genre;
import toonpick.app.entity.RefreshToken;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.jwt.JwtTokenProvider;
import toonpick.app.repository.RefreshTokenRepository;

import java.util.Date;


@Service
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(RefreshTokenRepository refreshTokenRepository, JwtTokenProvider jwtTokenProvider){
        this. refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void saveRefreshToken(String username, String refresh) {

        Date date = new Date(System.currentTimeMillis() + 86400000L);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .username(username)
                .token(refresh)
                .expiration(date.toString())
                .build();

        refreshTokenRepository.save(refreshToken);
    }


    @Transactional
    public String refreshAccessToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (jwtTokenProvider.isExpired(refreshToken)) {
            throw new RuntimeException("Refresh token is expired");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        return jwtTokenProvider.createAccessToken(username, role);
    }

    @Transactional
    public String refreshRefreshToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);
        String token = jwtTokenProvider.createAccessToken(username, role);
        deleteRefreshToken(refreshToken);
        saveRefreshToken(username,token);
        return token;
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenRepository.deleteByToken(refreshToken);
    }
}