package toonpick.app.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.auth.jwt.JwtTokenValidator;
import toonpick.app.entity.RefreshToken;
import toonpick.app.auth.jwt.JwtTokenProvider;
import toonpick.app.repository.RefreshTokenRepository;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenValidator jwtTokenValidator;

    // Access Token 갱신
    @Transactional
    public String renewAccessToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Access Token 재발급
        Long userid = jwtTokenProvider.getUserId(refreshToken);
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(userid, username, role);

        return newAccessToken;
    }

    // Refresh Token 갱신
    @Transactional
    public String renewRefreshToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Refresh 토큰 재발급
        Long userid = jwtTokenProvider.getUserId(refreshToken);
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newRefreshToken = jwtTokenProvider.createAccessToken(userid, username, role);

        // Refresh Token 저장소에 갱신
        deleteRefreshToken(refreshToken);
        saveRefreshToken(username, newRefreshToken);

        return newRefreshToken;
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
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenRepository.deleteByToken(refreshToken);
    }

}
