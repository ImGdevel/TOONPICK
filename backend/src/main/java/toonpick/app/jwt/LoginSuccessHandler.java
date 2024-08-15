package toonpick.app.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import toonpick.app.service.AuthService;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public LoginSuccessHandler( JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("Success!");

        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        //토큰 생성
        String access = jwtTokenProvider.createAccessToken(username, role);
        String refresh = jwtTokenProvider.createRefreshToken(username, role);

        //Refresh 토큰 저장
        authService.saveRefreshToken(username, refresh);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(jwtTokenProvider.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

}
