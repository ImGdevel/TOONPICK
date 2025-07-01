package com.toonpick.test.unit.auth.service;

import com.toonpick.auth.service.AuthTokenService;
import com.toonpick.internal.security.exception.InvalidJwtTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.toonpick.auth.controller.TokenReissueController;
import com.toonpick.internal.security.jwt.JwtTokenProvider;
import com.toonpick.internal.security.jwt.JwtTokenValidator;
import com.toonpick.internal.security.utils.CookieUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenReissueControllerTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthTokenService authTokenService;

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

        try (MockedStatic<CookieUtils> mockedStatic = Mockito.mockStatic(CookieUtils.class)) {
            mockedStatic
                    .when(() -> CookieUtils.extractCookiesFromRequest(request, "refresh"))
                    .thenReturn(refreshToken);

            doNothing().when(jwtTokenValidator).validateRefreshToken(refreshToken);
            when(authTokenService.reissueAccessToken(refreshToken)).thenReturn(newAccessToken);
            when(jwtTokenProvider.isRefreshTokenAboutToExpire(refreshToken)).thenReturn(false);

            ResponseEntity<?> responseEntity = tokenReissueController.reissue(request, response);

            verify(response).setHeader("Authorization", "Bearer " + newAccessToken);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
    }

    @Test
    @DisplayName("유효하지 않은 Refresh Token으로 요청 시 오류 발생")
    void testReissue_InvalidRefreshToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String invalidRefreshToken = "invalid-refresh-token";

        try (MockedStatic<CookieUtils> mockedStatic = Mockito.mockStatic(CookieUtils.class)) {
            mockedStatic
                .when(() -> CookieUtils.extractCookiesFromRequest(request, "refresh"))
                .thenReturn(invalidRefreshToken);

            // jwtTokenValidator에서 예외 발생하도록 설정
            doThrow(new InvalidJwtTokenException("Invalid token"))
                .when(jwtTokenValidator).validateRefreshToken(invalidRefreshToken);

            // when & then
            assertThrows(InvalidJwtTokenException.class, () -> {
                tokenReissueController.reissue(request, response);
            });
        }
    }


    @Test
    @DisplayName("Access Token 갱신 시 Refresh Token 만료 임박으로 새 Refresh Token 발급")
    void testReissue_RefreshTokenRenewal() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";

        try (MockedStatic<CookieUtils> mockedStatic = Mockito.mockStatic(CookieUtils.class)) {
            mockedStatic
                    .when(() -> CookieUtils.extractCookiesFromRequest(request, "refresh"))
                    .thenReturn(refreshToken);
            mockedStatic
                    .when(() -> CookieUtils.createRefreshCookie(newRefreshToken))
                    .thenReturn(new Cookie("refresh", newRefreshToken));

            doNothing().when(jwtTokenValidator).validateRefreshToken(refreshToken);
            when(authTokenService.reissueAccessToken(refreshToken)).thenReturn(newAccessToken);
            when(jwtTokenProvider.isRefreshTokenAboutToExpire(refreshToken)).thenReturn(true);
            when(authTokenService.reissueRefreshToken(refreshToken)).thenReturn(newRefreshToken);

            ResponseEntity<?> responseEntity = tokenReissueController.reissue(request, response);

            verify(response).setHeader("Authorization", "Bearer " + newAccessToken);
            verify(response).addCookie(any(Cookie.class));
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
    }


}
