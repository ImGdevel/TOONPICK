package com.toonpick.internal.security.jwt;


import com.toonpick.internal.security.exception.ExpiredJwtTokenException;
import com.toonpick.internal.security.exception.InvalidJwtTokenException;
import com.toonpick.internal.security.exception.MissingJwtTokenException;
import com.toonpick.common.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    // Access Token 검증
    public void validateAccessToken(String token) {
        if (jwtTokenProvider.isExpired(token)) {
            throw new ExpiredJwtTokenException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        if (!"access".equals(jwtTokenProvider.getCategory(token))) {
            throw new InvalidJwtTokenException(ErrorCode.ACCESS_TOKEN_INVALID);
        }
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

}
