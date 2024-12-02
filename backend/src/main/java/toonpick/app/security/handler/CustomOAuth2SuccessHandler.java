package toonpick.app.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import toonpick.app.dto.CustomOAuth2User;
import toonpick.app.security.jwt.JwtTokenProvider;
import toonpick.app.service.AuthService;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2SuccessHandler.class);

    public CustomOAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");

        Long userid = authService.getUserIdByUsername(username);
        String refreshToken = jwtTokenProvider.createRefreshToken(userid, username, role);

        authService.saveRefreshToken(username, refreshToken);

        response.addCookie(jwtTokenProvider.createCookie("refresh", refreshToken));

        response.sendRedirect("http://localhost:3000/refresh");
    }

}
