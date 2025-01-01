package toonpick.app.auth.jwt;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import toonpick.app.auth.user.CustomUserDetails;

@Component
public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenValidator(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Access Token 추출
    public String extractAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return null;
    }

    // Access Token 검증
    public void validateAccessToken(String token) {
        if (jwtTokenProvider.isExpired(token)) {
            throw new IllegalArgumentException("Access token expired");
        }
        if (!"access".equals(jwtTokenProvider.getCategory(token))) {
            throw new IllegalArgumentException("Invalid access token category");
        }
    }

    // Refresh Token 추출
    public String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Refresh Token 검증
    public void validateRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token is missing.");
        }

        if (jwtTokenProvider.isExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh token expired.");
        }

        if (!"refresh".equals(jwtTokenProvider.getCategory(refreshToken))) {
            throw new IllegalArgumentException("Invalid refresh token category.");
        }
    }

    public UserDetails getUserDetails(String token) {
        String username = jwtTokenProvider.getUsername(token);
        String role = jwtTokenProvider.getRole(token);
        return new CustomUserDetails(username, "dummyPassword", role);
    }
}
