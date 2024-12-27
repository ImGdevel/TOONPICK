package toonpick.app.auth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import toonpick.app.auth.service.AuthService;

@Component
public class LogoutHandler {

    private final AuthService authService;

    public LogoutHandler(AuthService authService) {
        this.authService = authService;
    }

    public void handleLogout(String refreshToken, HttpServletResponse response) {

        authService.deleteRefreshToken(refreshToken);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
