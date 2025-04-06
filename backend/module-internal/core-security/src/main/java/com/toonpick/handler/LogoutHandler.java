package com.toonpick.handler;

import com.toonpick.utils.CookieUtils;
import com.toonpick.jwt.TokenIssuer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LogoutHandler {

    private final TokenIssuer tokenIssuer;

    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);


    public void handleLogout(String refreshToken, HttpServletResponse response) {

        tokenIssuer.deleteRefreshToken(refreshToken);

        response.addCookie(CookieUtils.createEmptyCookie("refresh"));

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
