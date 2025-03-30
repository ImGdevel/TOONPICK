package com.toonpick.test.unit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import com.toonpick.jwt.JwtTokenProvider;
import com.toonpick.jwt.JwtTokenValidator;

@SpringBootTest
class JwtTokenValidatorTest {

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("AccessToken 유효성 검증")
    void testValidateAccessToken() {
        String username = "testUser";
        String role = "USER";

        String accessToken = jwtTokenProvider.createAccessToken(username, role);

        Assertions.assertDoesNotThrow(() -> jwtTokenValidator.validateAccessToken(accessToken));
    }

    @Test
    @DisplayName("RefreshToken 유효성 검증")
    void testValidateRefreshToken() {
        String username = "testUser";
        String role = "USER";

        String refreshToken = jwtTokenProvider.createRefreshToken(username, role);

        Assertions.assertDoesNotThrow(() -> jwtTokenValidator.validateRefreshToken(refreshToken));
    }

    @Test
    @DisplayName("AccessToken 추출")
    void testExtractAccessToken() {
        String header = "Bearer sample-access-token";

        String extractedToken = jwtTokenValidator.extractAccessToken(header);

        Assertions.assertEquals("sample-access-token", extractedToken);
    }

    @Test
    @DisplayName("잘못된 AccessToken 추출")
    void testExtractInvalidAccessToken() {
        String header = "InvalidHeader";

        String extractedToken = jwtTokenValidator.extractAccessToken(header);

        Assertions.assertNull(extractedToken);
    }

    @Test
    @DisplayName("RefreshToken 쿠키에서 추출")
    void testExtractRefreshTokenFromCookies() {
        Cookie cookie = new Cookie("refresh", "sample-refresh-token");
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookie});

        String refreshToken = jwtTokenValidator.extractRefreshTokenFromCookies(mockRequest);

        Assertions.assertEquals("sample-refresh-token", refreshToken);
    }

    @Test
    @DisplayName("쿠키에서 RefreshToken 없을 때 검증")
    void testExtractRefreshTokenFromCookiesWhenMissing() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockRequest.getCookies()).thenReturn(new Cookie[]{});

        String refreshToken = jwtTokenValidator.extractRefreshTokenFromCookies(mockRequest);

        Assertions.assertNull(refreshToken);
    }

    @Test
    @DisplayName("Token으로 UserDetails 생성")
    void testGetUserDetails() {
        String username = "testUser";
        String role = "USER";

        String token = jwtTokenProvider.createAccessToken(username, role);

        UserDetails userDetails = jwtTokenValidator.getUserDetails(token);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(username, userDetails.getUsername());
        Assertions.assertEquals(role, userDetails.getAuthorities().iterator().next().getAuthority());
    }
}
