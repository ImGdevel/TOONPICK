package com.toonpick.auth.service;

import com.toonpick.exception.RefreshTokenNotFoundException;
import com.toonpick.jwt.TokenIssuer;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.toonpick.jwt.JwtTokenProvider;
import com.toonpick.jwt.JwtTokenValidator;
import com.toonpick.service.RefreshTokenService;


@Service
@RequiredArgsConstructor
public class AuthTokenService implements TokenIssuer {

    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenValidator jwtTokenValidator;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenService.class);

    /**
     * Access Token 발급
     */
    public String issueAccessToken(String username, String role) {
        return jwtTokenProvider.createAccessToken(username, role);
    }

    /**
     * Refresh Token 발급
     */
    public String issueRefreshToken(String username, String role) {
        String refreshToken = jwtTokenProvider.createRefreshToken(username, role);
        refreshTokenService.saveToken(refreshToken, username, jwtTokenProvider.getExpiration(refreshToken).toString());
        return refreshToken;
    }

    /**
     * Access Token 재발급
     */
    public String reissueAccessToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Access Token 재발급
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        return jwtTokenProvider.createAccessToken(username, role);
    }

    /**
     * Refresh Token 재발급
     */
    public String reissueRefreshToken(String refreshToken) {
        // Refresh 토큰 유효 검증
        refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        jwtTokenValidator.validateRefreshToken(refreshToken);

        // Refresh 토큰 재발급
        String username = jwtTokenProvider.getUsername(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);

        String newRefreshToken = jwtTokenProvider.createRefreshToken(username, role);

        // Refresh Token 저장소에 갱신
        refreshTokenService.deleteToken(refreshToken);
        refreshTokenService.saveToken(newRefreshToken, username, jwtTokenProvider.getExpiration(newRefreshToken).toString());

        return newRefreshToken;
    }

    /**
     * 토큰 삭제
     */
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenService.deleteToken(refreshToken);
    }
}
