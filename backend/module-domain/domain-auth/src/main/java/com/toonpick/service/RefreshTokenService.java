package com.toonpick.service;

import com.toonpick.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.toonpick.entity.RefreshToken;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 리프레시 토큰 저장
     */
    public void saveToken(String token, String username, String expiration) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .username(username)
                .expiration(expiration)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 리프레시 토큰 삭제
     */
    public void deleteToken(String token) {
        refreshTokenRepository.deleteById(token);
    }

    /**
     * 리프레시 토큰 조회
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findById(token);
    }

    /**
     * 유저의 리프레시 토큰이 유효한지 확인
     */
    public boolean isValid(String token, String username) {
        return refreshTokenRepository.findById(token)
                .map(rt -> rt.getUsername().equals(username))
                .orElse(false);
    }
}
