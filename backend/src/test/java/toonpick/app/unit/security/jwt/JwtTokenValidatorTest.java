package toonpick.app.unit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import toonpick.app.exception.exception.ExpiredJwtTokenException;
import toonpick.app.exception.exception.InvalidJwtTokenException;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.security.jwt.JwtTokenValidator;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenValidatorTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private JwtTokenValidator jwtTokenValidator;

    @Test
    @DisplayName("AccessToken 유효성 검증")
    void testValidateAccessToken() {
        String username = "testUser";
        String role = "USER";
        String accessToken = "validAccessToken";

        when(jwtTokenProvider.isExpired(accessToken)).thenReturn(false);
        when(jwtTokenProvider.getCategory(accessToken)).thenReturn("access");

        assertDoesNotThrow(() -> jwtTokenValidator.validateAccessToken(accessToken));
    }

    @Test
    @DisplayName("RefreshToken 유효성 검증")
    void testValidateRefreshToken() {
        String username = "testUser";
        String role = "USER";
        String refreshToken = "validRefreshToken"; // 임의의 유효한 refresh 토큰 값 설정

        // JwtTokenProvider의 메소드 mock 설정
        when(jwtTokenProvider.isExpired(refreshToken)).thenReturn(false);
        when(jwtTokenProvider.getCategory(refreshToken)).thenReturn("refresh");

        assertDoesNotThrow(() -> jwtTokenValidator.validateRefreshToken(refreshToken));
    }

    @Test
    @DisplayName("AccessToken 추출")
    void testExtractAccessToken() {
        String header = "Bearer sample-access-token";

        String extractedToken = jwtTokenValidator.extractAccessToken(header);

        assertEquals("sample-access-token", extractedToken);
    }

    @Test
    @DisplayName("잘못된 AccessToken 추출")
    void testExtractInvalidAccessToken() {
        String header = "InvalidHeader";

        String extractedToken = jwtTokenValidator.extractAccessToken(header);

        assertNull(extractedToken);
    }

    @Test
    @DisplayName("RefreshToken 쿠키에서 추출")
    void testExtractRefreshTokenFromCookies() {
        Cookie cookie = new Cookie("refresh", "sample-refresh-token");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{cookie});

        String refreshToken = jwtTokenValidator.extractRefreshTokenFromCookies(mockRequest);

        assertEquals("sample-refresh-token", refreshToken);
    }

    @Test
    @DisplayName("쿠키에서 RefreshToken 없을 때 검증")
    void testExtractRefreshTokenFromCookiesWhenMissing() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getCookies()).thenReturn(new Cookie[]{});

        String refreshToken = jwtTokenValidator.extractRefreshTokenFromCookies(mockRequest);

        assertNull(refreshToken);
    }

    @Test
    @DisplayName("Token으로 UserDetails 생성")
    void testGetUserDetails() {
        String username = "testUser";
        String role = "USER";
        String token = "validAccessToken";

        when(jwtTokenProvider.getUsername(token)).thenReturn(username);
        when(jwtTokenProvider.getRole(token)).thenReturn(role);

        UserDetails userDetails = jwtTokenValidator.getUserDetails(token);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(role, userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    @DisplayName("AccessToken 만료 예외 처리")
    void testValidateExpiredAccessToken() {
        String accessToken = "expiredAccessToken";

        when(jwtTokenProvider.isExpired(accessToken)).thenReturn(true);

        assertThrows(ExpiredJwtTokenException.class, () -> jwtTokenValidator.validateAccessToken(accessToken));
    }

    @Test
    @DisplayName("잘못된 RefreshToken 처리")
    void testValidateInvalidRefreshToken() {
        String refreshToken = "invalidRefreshToken";

        when(jwtTokenProvider.getCategory(refreshToken)).thenReturn("invalid");

        assertThrows(InvalidJwtTokenException.class, () -> jwtTokenValidator.validateRefreshToken(refreshToken));
    }
}
