package toonpick.app.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.core.step.item.FaultTolerantChunkProcessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import toonpick.app.dto.CustomOAuth2User;
import toonpick.app.entity.RefreshToken;
import toonpick.app.jwt.JwtTokenProvider;
import toonpick.app.repository.RefreshTokenRepository;
import toonpick.app.service.AuthService;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public CustomSuccessHandler(JwtTokenProvider jwtTokenProvider, AuthService authService) {
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
        String token = jwtTokenProvider.createRefreshToken(userid, username, role);

        authService.saveRefreshToken(username, token);

        response.addCookie(jwtTokenProvider.createCookie("refresh", token));

        response.sendRedirect("http://localhost:3000/refresh");
    }

}
