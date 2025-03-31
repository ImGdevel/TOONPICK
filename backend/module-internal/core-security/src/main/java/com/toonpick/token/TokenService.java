package com.toonpick.token;

import com.toonpick.exception.RefreshTokenNotFoundException;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.toonpick.jwt.JwtTokenProvider;
import com.toonpick.jwt.JwtTokenValidator;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenValidator jwtTokenValidator;

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    /**
     * Access Token 발급
     */
    public String issueAccessToken(String username, String role){
        String newAccessToken = jwtTokenProvider.createAccessToken(username, role);
        return newAccessToken;
    }

    /**
     * Refresh Token 발급
     */
    public String issueRefreshToken(String username, String role){
        String newRefreshToken = jwtTokenProvider.createRefreshToken(username,role);
        saveRefreshToken(username, newRefreshToken);
        return newRefreshToken;
    }

    /**
     * Access Token 재발급
     */
    public String reissueAccessToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Access Token 재발급
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newAccessToken = jwtTokenProvider.createAccessToken(username, role);

        return newAccessToken;
    }

    /**
     * Refresh Token 재발급
     */
    public String reissueRefreshToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Refresh 토큰 재발급
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newRefreshToken = jwtTokenProvider.createRefreshToken(username, role);

        // Refresh Token 저장소에 갱신
        deleteRefreshToken(refreshToken);
        saveRefreshToken(username, newRefreshToken);

        return newRefreshToken;
    }

    public void saveRefreshToken(String username, String refresh) {
        Date expirationDate = jwtTokenProvider.getExpiration(refresh);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refresh)
                .username(username)
                .expiration(expirationDate.toString())
                .build();

        refreshTokenRepository.save(refreshToken);

    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenRepository.deleteById(refreshToken);
    }

}
