
package com.toonpick.filter;

import com.toonpick.exception.ExpiredJwtTokenException;
import com.toonpick.exception.InvalidJwtTokenException;
import com.toonpick.handler.LogoutHandler;
import com.toonpick.utils.ErrorResponseSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;
import com.toonpick.jwt.JwtTokenValidator;


import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtTokenValidator tokenValidator;
    private final LogoutHandler logoutHandler;
    private final ErrorResponseSender errorResponseSender;

    private final Logger logger = LoggerFactory.getLogger(CustomLogoutFilter.class);

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
            String refreshToken = tokenValidator.extractRefreshTokenFromCookies(httpRequest);
            if (refreshToken != null) {
                try {
                    tokenValidator.validateRefreshToken(refreshToken);
                } catch (InvalidJwtTokenException | ExpiredJwtTokenException e) {
                    logger.warn("Ignoring invalid/expired refresh token during logout: {}", e.getMessage());
                }
            }

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
