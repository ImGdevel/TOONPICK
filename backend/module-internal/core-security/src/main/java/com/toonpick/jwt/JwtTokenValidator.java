package com.toonpick.jwt;


import com.toonpick.exception.ExpiredJwtTokenException;
import com.toonpick.exception.InvalidJwtTokenException;
import com.toonpick.exception.MissingJwtTokenException;
import com.toonpick.type.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.toonpick.dto.CustomUserDetails;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    // Access Token 추출
    public String extractAccessToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7).trim();
    }

    // Access Token 검증
    public void validateAccessToken(String token) {
        if (jwtTokenProvider.isExpired(token)) {
            throw new ExpiredJwtTokenException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        if (!"access".equals(jwtTokenProvider.getCategory(token))) {
            throw new InvalidJwtTokenException(ErrorCode.ACCESS_TOKEN_INVALID);
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
            throw new MissingJwtTokenException(ErrorCode.REFRESH_TOKEN_MISSING);
        }
        if (!"refresh".equals(jwtTokenProvider.getCategory(refreshToken))) {
            throw new InvalidJwtTokenException(ErrorCode.REFRESH_TOKEN_INVALID, jwtTokenProvider.getCategory(refreshToken));
        }
        if (jwtTokenProvider.isExpired(refreshToken)) {
            throw new ExpiredJwtTokenException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    // todo : 잘못된 위치 -> 이동 시킬 것
    // 유저 정보 추출
    public UserDetails getUserDetails(String token) {
        String username = jwtTokenProvider.getUsername(token);
        String role = jwtTokenProvider.getRole(token);
        return new CustomUserDetails(username, "dummyPassword", role);
    }
}
