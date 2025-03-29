package toonpick.handler;

import jakarta.servlet.ServletException;
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
import toonpick.user.CustomOAuth2UserDetails;
import toonpick.token.TokenService;
import toonpick.utils.CookieUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2UserDetails customUserDetails = (CustomOAuth2UserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String role = authentication.getAuthorities().stream().
                findFirst().map(GrantedAuthority::getAuthority).orElse("");

        String refreshToken = tokenService.issueRefreshToken(username, role);
        response.addCookie(CookieUtils.createRefreshCookie(refreshToken));
        response.setStatus(HttpStatus.OK.value());
        logger.info("USER LOGIN SUCCESS (username-{})", username);

        response.sendRedirect("http://localhost:3000/refresh");
    }

}
