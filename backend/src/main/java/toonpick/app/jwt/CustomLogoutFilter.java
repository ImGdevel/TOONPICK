package toonpick.app.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.Token;
import org.springframework.web.filter.GenericFilterBean;
import toonpick.app.repository.RefreshTokenRepository;
import toonpick.app.service.AuthService;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public CustomLogoutFilter(JwtTokenProvider jwtTokenProvider, AuthService authService) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }


    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // path와 method 검증
        if (!request.getRequestURI().matches("^\\/logout$") || !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 refresh token 추출
        String refreshToken = getRefreshTokenFromCookies(request);
        // 토큰이 없을 경우
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰 만료 확인
        if (jwtTokenProvider.isExpired(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!jwtTokenProvider.getCategory(refreshToken).equals("refresh")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            // Refresh 토큰 DB에서 제거
            authService.deleteRefreshToken(refreshToken);

            // Refresh 토큰 Cookie 값 0
            Cookie cookie = new Cookie("refresh", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");

            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // 쿠키에서 refresh token 추출
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
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

}
