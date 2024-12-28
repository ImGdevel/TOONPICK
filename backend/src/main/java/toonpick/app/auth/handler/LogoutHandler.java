package toonpick.app.auth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import toonpick.app.auth.service.TokenService;

@Component
public class LogoutHandler {

    private final TokenService tokenService;

    public LogoutHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void handleLogout(String refreshToken, HttpServletResponse response) {

        tokenService.deleteRefreshToken(refreshToken);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
