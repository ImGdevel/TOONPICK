package toonpick.handler;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import toonpick.token.TokenService;
import toonpick.utils.CookieUtils;

@Component
@RequiredArgsConstructor
public class LogoutHandler {

    private final TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);


    public void handleLogout(String refreshToken, HttpServletResponse response) {

        tokenService.deleteRefreshToken(refreshToken);

        response.addCookie(CookieUtils.createEmptyCookie("refresh"));

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
