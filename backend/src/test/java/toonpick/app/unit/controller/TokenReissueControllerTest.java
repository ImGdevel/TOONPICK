package toonpick.app.unit.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import toonpick.app.controller.TokenReissueController;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenReissueControllerTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthService authService;

    @Mock
    private JwtTokenValidator jwtTokenValidator;

    @InjectMocks
    private TokenReissueController tokenReissueController;

    @Test
    @DisplayName("유효한 Refresh Token으로 Access Token 갱신")
    void testReissue_SuccessfulAccessTokenRenewal() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";

        when(jwtTokenValidator.extractRefreshTokenFromCookies(request)).thenReturn(refreshToken);
        doNothing().when(jwtTokenValidator).validateRefreshToken(refreshToken);
        when(authService.refreshAccessToken(refreshToken)).thenReturn(newAccessToken);
        when(jwtTokenProvider.isRefreshTokenAboutToExpire(refreshToken)).thenReturn(false);

        ResponseEntity<?> responseEntity = tokenReissueController.reissue(request, response);

        verify(response).setHeader("Authorization", "Bearer " + newAccessToken);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("유효하지 않은 Refresh Token으로 요청 시 오류 발생")
    void testReissue_InvalidRefreshToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String invalidRefreshToken = "invalid-refresh-token";

        when(jwtTokenValidator.extractRefreshTokenFromCookies(request)).thenReturn(invalidRefreshToken);
        doThrow(new IllegalArgumentException("Invalid token")).when(jwtTokenValidator).validateRefreshToken(invalidRefreshToken);

        ResponseEntity<?> responseEntity = tokenReissueController.reissue(request, response);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid token", responseEntity.getBody());
    }

    @Test
    @DisplayName("Access Token 갱신 시 Refresh Token 만료 임박으로 새 Refresh Token 발급")
    void testReissue_RefreshTokenRenewal() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";

        when(jwtTokenValidator.extractRefreshTokenFromCookies(request)).thenReturn(refreshToken);
        doNothing().when(jwtTokenValidator).validateRefreshToken(refreshToken);
        when(authService.refreshAccessToken(refreshToken)).thenReturn(newAccessToken);
        when(jwtTokenProvider.isRefreshTokenAboutToExpire(refreshToken)).thenReturn(true);
        when(authService.refreshRefreshToken(refreshToken)).thenReturn(newRefreshToken);
        when(jwtTokenProvider.createCookie("refresh", newRefreshToken)).thenReturn(new Cookie("refresh", newRefreshToken));

        ResponseEntity<?> responseEntity = tokenReissueController.reissue(request, response);

        verify(response).setHeader("Authorization", "Bearer " + newAccessToken);
        verify(response).addCookie(any(Cookie.class));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
