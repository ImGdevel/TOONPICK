package toonpick.app.unit.security.jwt;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import toonpick.app.security.jwt.JwtTokenProvider;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String secretKeyString = "test_secret_key_dsakjfhaiuefabeiwfaidiae";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

    private final long accessTokenExpiration = 3600000;
    private final long refreshTokenExpiration = 604800000;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKeyString, accessTokenExpiration, refreshTokenExpiration);
    }

    @Test
    @DisplayName("AccessToken 생성 및 유효성 검증")
    void testCreateAccessTokenAndValidate() {
        String username = "testUser";
        String role = "USER";

        String token = jwtTokenProvider.createAccessToken(username, role);

        Assertions.assertNotNull(token);
        Assertions.assertEquals(username, jwtTokenProvider.getUsername(token));
        Assertions.assertEquals(role, jwtTokenProvider.getRole(token));
        Assertions.assertFalse(jwtTokenProvider.isExpired(token));
    }

    @Test
    @DisplayName("RefreshToken 생성 및 만료 임박 여부 확인")
    void testIsRefreshTokenAboutToExpire() {
        String username = "testUser";
        String role = "USER";

        String refreshToken = jwtTokenProvider.createRefreshToken(username, role);

        boolean isAboutToExpire = jwtTokenProvider.isRefreshTokenAboutToExpire(refreshToken);

        Assertions.assertFalse(isAboutToExpire);
    }

    @Test
    @DisplayName("만료된 토큰 검증")
    void testIsExpiredToken() {
        String expiredToken = Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(secretKey)
                .compact();

        Assertions.assertTrue(jwtTokenProvider.isExpired(expiredToken));
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증")
    void testInvalidToken() {
        String invalidToken = "invalid.token";

        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.getCategory(invalidToken));
    }

    @Test
    @DisplayName("토큰의 Claims에서 특정 값 추출")
    void testGetClaims() {
        String username = "testUser";
        String role = "USER";

        String token = jwtTokenProvider.createAccessToken(username, role);

        Assertions.assertEquals(username, jwtTokenProvider.getUsername(token));
        Assertions.assertEquals(role, jwtTokenProvider.getRole(token));
    }

    @Test
    @DisplayName("토큰에서 만료 시간 확인")
    void testGetExpiration() {
        String username = "testUser";
        String role = "USER";

        String token = jwtTokenProvider.createAccessToken(username, role);

        Date expiration = jwtTokenProvider.getExpiration(token);

        Assertions.assertNotNull(expiration);
        Assertions.assertTrue(expiration.after(new Date()));
    }
}
