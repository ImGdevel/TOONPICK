package toonpick.app.security.jwt;


import org.springframework.security.core.userdetails.UserDetails;
import toonpick.app.dto.CustomUserDetails;

public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenValidator(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return null;
    }

    public void validateToken(String token) {
        if (jwtTokenProvider.isExpired(token)) {
            throw new IllegalArgumentException("Access token expired");
        }
        if (!"access".equals(jwtTokenProvider.getCategory(token))) {
            throw new IllegalArgumentException("Invalid access token category");
        }
    }

    public UserDetails getUserDetails(String token) {
        Long userId = jwtTokenProvider.getUserId(token);
        String username = jwtTokenProvider.getUsername(token);
        String role = jwtTokenProvider.getRole(token);
        return new CustomUserDetails(userId, username, "dummyPassword", role);
    }
}
