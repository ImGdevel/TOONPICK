package com.toonpick.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    private final long accessTokenExpiration;

    private final long refreshTokenExpiration;

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret,
                            @Value("${spring.jwt.access-token-expiration}") long accessTokenExpiration,
                            @Value("${spring.jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 토큰에서 유형 추출
    public String getCategory(String token) {
        return getClaims(token).get("category", String.class);
    }

    // 토큰에서 사용자 이름 추출
    public String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    // 사용자 권한 추출
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 토큰 만료 시간 반환
    public Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // 토큰 인증 만료 여부 체크
    public boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    // Refresh 토큰 만료 임박 여부 확인
    public boolean isRefreshTokenAboutToExpire(String refreshToken) {
        Date expirationDate = getExpiration(refreshToken);
        long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        return remainingTime < oneDayInMillis;
    }

    // Access Token 생성
    public String createAccessToken(String username, String role) {
        return createToken("access", username, role, accessTokenExpiration);
    }

    // Refresh Token 생성
    public String createRefreshToken(String username, String role) {
        return createToken("refresh", username, role, refreshTokenExpiration);
    }

    // 검증/확인을 위한 추출 메서드
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 생성
    private String createToken(String category, String username, String role, Long expirationTime) {
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

}
