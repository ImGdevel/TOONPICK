package toonpick.app.security.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import toonpick.app.security.token.TokenService;

@Component
@RequiredArgsConstructor
public class LogoutHandler {

    private final TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);


    public void handleLogout(String refreshToken, HttpServletResponse response) {

        tokenService.deleteRefreshToken(refreshToken);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
