package toonpick.app.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import toonpick.app.security.token.TokenService;
import toonpick.app.utils.CookieUtils;

import java.util.stream.Collectors;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        String accessToken = tokenService.issueAccessToken(username, role);
        String refreshToken = tokenService.issueRefreshToken(username, role);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(CookieUtils.createRefreshCookie(refreshToken));
        response.setStatus(HttpStatus.OK.value());
        logger.info("USER LOGIN SUCCESS (username-{})", username);
    }
}
