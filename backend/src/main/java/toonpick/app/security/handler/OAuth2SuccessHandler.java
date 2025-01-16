package toonpick.app.security.handler;

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
import toonpick.app.security.user.CustomOAuth2UserDetails;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.security.token.TokenService;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2UserDetails customUserDetails = (CustomOAuth2UserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");

        String refreshToken = jwtTokenProvider.createRefreshToken( username, role);
        String accessToken = jwtTokenProvider.createAccessToken(username, role);

        tokenService.saveRefreshToken(username, refreshToken);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(jwtTokenProvider.createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        response.sendRedirect("http://localhost:3000/refresh");
    }

}
