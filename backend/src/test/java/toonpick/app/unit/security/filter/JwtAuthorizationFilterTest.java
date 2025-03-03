package toonpick.app.unit.security.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import toonpick.app.exception.exception.ExpiredJwtTokenException;
import toonpick.app.security.filter.JwtAuthorizationFilter;
import toonpick.app.security.jwt.JwtTokenValidator;
import toonpick.app.utils.ErrorResponseSender;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class JwtAuthorizationFilterTest {
    @Mock
    private JwtTokenValidator jwtTokenValidator;
    @Mock
    private ErrorResponseSender errorResponseSender;
    @Mock
    private FilterChain filterChain;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Public 경로 요청 시 필터 건너뜀")
    public void testDoFilterInternal_SkipPublicPaths() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/public/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtTokenValidator);
    }

    @Test
    @DisplayName("유효한 토큰 인증")
    public void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", "Bearer valid.token.here");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtTokenValidator.extractAccessToken(any())).thenReturn("valid.token.here");
        when(jwtTokenValidator.getUserDetails(any())).thenReturn(userDetails);

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenValidator, times(1)).validateAccessToken("valid.token.here");
        verify(jwtTokenValidator, times(1)).getUserDetails("valid.token.here");
        verify(filterChain, times(1)).doFilter(request, response);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(userDetails, authentication.getPrincipal());
    }

    @Test
    @DisplayName("만료된 토큰 예외 처리")
    public void testDoFilterInternal_ExpiredToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/protected");
        request.addHeader("Authorization", "Bearer expired.token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtTokenValidator.extractAccessToken(any())).thenReturn("expired.token");
        doThrow(new ExpiredJwtTokenException("Token expired")).when(jwtTokenValidator).validateAccessToken("expired.token");

        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenValidator, times(1)).validateAccessToken("expired.token");
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}
