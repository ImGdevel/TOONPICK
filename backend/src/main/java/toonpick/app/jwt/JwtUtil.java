package toonpick.app.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    private final long accessTokenExpiration;

    private final long refreshTokenExpiration;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret,
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

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Access Token 생성
    public String createAccessToken(String username, String role) {
        return createToken("access" , username,role, accessTokenExpiration);
    }

    // Refresh Token 생성
    public String createRefreshToken(String username, String role) {
        return createToken( "refresh",username,role, refreshTokenExpiration);
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

    // 검증/확인을 위한 추출 메서드
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 쿠키 생성 메서드
    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}