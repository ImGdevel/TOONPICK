package com.toonpick.auth.handler;

import com.toonpick.internal.security.utils.CookieUtils;
import com.toonpick.internal.security.handler.LoginSuccessHandler;
import com.toonpick.internal.security.jwt.TokenIssuer;
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


import java.util.stream.Collectors;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler implements LoginSuccessHandler {

    private final TokenIssuer tokenIssuer;

    private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandlerImpl.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        String accessToken = tokenIssuer.issueAccessToken(username, role);
        String refreshToken = tokenIssuer.issueRefreshToken(username, role);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(CookieUtils.createRefreshCookie(refreshToken));
        response.setStatus(HttpStatus.OK.value());
        logger.info("USER LOGIN SUCCESS (username-{})", username);
    }
}
