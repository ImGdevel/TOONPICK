package toonpick.app.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import toonpick.app.auth.user.CustomOAuth2UserDetails;
import toonpick.app.auth.jwt.JwtTokenProvider;
import toonpick.app.auth.token.TokenService;
import toonpick.app.service.MemberService;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final MemberService memberService;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2UserDetails customUserDetails = (CustomOAuth2UserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");

        Long userid = memberService.getUserIdByUsername(username);
        String refreshToken = jwtTokenProvider.createRefreshToken(userid, username, role);

        tokenService.saveRefreshToken(username, refreshToken);

        response.addCookie(jwtTokenProvider.createCookie("refresh", refreshToken));

        response.sendRedirect("http://localhost:3000/refresh");
    }

}
