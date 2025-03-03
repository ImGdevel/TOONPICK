package toonpick.app.unit.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import toonpick.app.controller.TokenReissueController;
import toonpick.app.exception.exception.ExpiredJwtTokenException;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.security.token.TokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TokenReissueControllerTest {
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private JwtTokenValidator jwtTokenValidator;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenReissueController tokenReissueController;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("유효한 Refresh 토큰으로 Access 토큰 재발급")
    public void testReissue_ValidToken() {
        when(jwtTokenValidator.extractRefreshTokenFromCookies(request)).thenReturn("valid.refresh.token");
        when(tokenService.reissueAccessToken(any())).thenReturn("new.access.token");

        ResponseEntity<String> result = tokenReissueController.reissue(request, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Bearer new.access.token", response.getHeader("Authorization"));
    }

    @Test
    @DisplayName("만료된 Refresh 토큰 예외 처리")
    public void testReissue_ExpiredToken() {
        when(jwtTokenValidator.extractRefreshTokenFromCookies(request)).thenThrow(new ExpiredJwtTokenException("Token expired"));

        ResponseEntity<String> result = tokenReissueController.reissue(request, response);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Token expired", result.getBody());
    }

    @Test
    @DisplayName("예기치 않은 오류 처리")
    public void testReissue_UnexpectedError() {
        when(jwtTokenValidator.extractRefreshTokenFromCookies(request)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<String> result = tokenReissueController.reissue(request, response);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Unexpected error", result.getBody());
    }
}
