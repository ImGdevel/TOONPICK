package com.toonpick.handler;

import com.toonpick.exception.ExpiredJwtTokenException;
import com.toonpick.exception.InvalidJwtTokenException;
import com.toonpick.jwt.JwtTokenValidator;
import com.toonpick.utils.CookieUtils;
import com.toonpick.jwt.TokenIssuer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LogoutHandler {

    private final TokenIssuer tokenIssuer;
    private final JwtTokenValidator tokenValidator;

    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    public void handleLogout(HttpServletRequest request, HttpServletResponse response) {

        // refresh 토큰이 존재하는지 확인하고 제거
        String refreshToken = CookieUtils.extractCookiesFromRequest(request, "refresh");
        if (refreshToken != null) {
            try {
                tokenValidator.validateRefreshToken(refreshToken);
            } catch (InvalidJwtTokenException | ExpiredJwtTokenException e) {
                logger.warn("Ignoring invalid/expired refresh token during logout: {}", e.getMessage());
            }
        }

        tokenIssuer.deleteRefreshToken(refreshToken);

        response.addCookie(CookieUtils.createEmptyCookie("refresh"));

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
