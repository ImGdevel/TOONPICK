
package toonpick.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;
import toonpick.handler.LogoutHandler;
import toonpick.jwt.JwtTokenValidator;
import toonpick.utils.ErrorResponseSender;


import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtTokenValidator tokenValidator;
    private final LogoutHandler logoutHandler;
    private final ErrorResponseSender errorResponseSender;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 요청과 응답이 HttpServletRequest, HttpServletResponse 타입 확인 및 캐스팅
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 로그아웃 요청인지 확인
        if (!isLogoutRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 쿠키에서 Refresh Token 추출및 검증
            String refreshToken = tokenValidator.extractRefreshTokenFromCookies(httpRequest);
            tokenValidator.validateRefreshToken(refreshToken);

            // 로그아웃
            logoutHandler.handleLogout(refreshToken, httpResponse);

        } catch (IllegalArgumentException e) {
            errorResponseSender.sendErrorResponse(httpResponse, "Invalid refresh token", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return "/logout".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod());
    }
}
