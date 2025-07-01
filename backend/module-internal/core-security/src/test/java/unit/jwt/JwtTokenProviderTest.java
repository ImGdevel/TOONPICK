package unit.jwt;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.toonpick.internal.security.jwt.JwtTokenProvider;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    JwtTokenProvider jwtTokenProvider;

    String secret = "jwtjwtjwtjwtjwtjwtjwtjwtjwtjwtjwtjwtjwtjwt";

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secret, 1000 * 60 * 10, 1000 * 60 * 60 * 25);
    }

    @Test
    @DisplayName("AccessToken 생성 및 유효성 검증")
    void createAndParseAccessToken() {
        String username = "testUser";
        String role = "USER";

        String token = jwtTokenProvider.createAccessToken(username, role);

        assertNotNull(token);
        assertEquals("access", jwtTokenProvider.getCategory(token));
        assertEquals(username, jwtTokenProvider.getUsername(token));
        assertEquals(role, jwtTokenProvider.getRole(token));
        assertFalse(jwtTokenProvider.isExpired(token));
    }

    @Test
    @DisplayName("잘못된 토큰 입력 시 예외 처리")
    void invalidTokenShouldThrowException() {
        String invalidToken = "twjtwjtwjtwjtwjtwjtwjtwjtwjtwjtwjtwjtwj";
        assertThrows(Exception.class, () -> jwtTokenProvider.getCategory(invalidToken));
        assertThrows(Exception.class, () -> jwtTokenProvider.getUsername(invalidToken));
        assertThrows(Exception.class, () -> jwtTokenProvider.getRole(invalidToken));
    }


    @Test
    @DisplayName("RefreshToken 생성 및 만료 임박 여부 확인")
    void createRefreshTokenAndCheckExpiration() {
        String token = jwtTokenProvider.createRefreshToken("user", "ROLE_USER");

        assertNotNull(token);
        assertFalse(jwtTokenProvider.isRefreshTokenAboutToExpire(token));
    }

    @Test
    @DisplayName("일부 claim이 없는 토큰 처리")
    void tokenWithoutCategory() {
        String token = Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()))
                .compact();

        // null 값
        assertNull(jwtTokenProvider.getCategory(token));
        assertNull(jwtTokenProvider.getUsername(token));
        assertNull(jwtTokenProvider.getRole(token));
    }

    @Test
    @DisplayName("AccessToken 만료 시간 확인")
    void accessTokenExpirationTest() {
        String token = jwtTokenProvider.createAccessToken("testUser", "USER");
        Date expiration = jwtTokenProvider.getExpiration(token);

        long now = System.currentTimeMillis();
        assertTrue(expiration.getTime() > now);
    }

    @Test
    @DisplayName("토큰 만료 검사")
    void testExpiredToken() {
        String expiredToken = Jwts.builder()
                .claim("category", "access")
                .claim("username", "user")
                .claim("role", "USER")
                .issuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10))
                .expiration(new Date(System.currentTimeMillis() - 1000 * 60 * 5))
                .signWith(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()))
                .compact();

        assertTrue(jwtTokenProvider.isExpired(expiredToken));
    }
}
