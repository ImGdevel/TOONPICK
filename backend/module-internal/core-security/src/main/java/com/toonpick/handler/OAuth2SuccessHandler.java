package com.toonpick.handler;

import com.toonpick.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.toonpick.user.CustomOAuth2UserDetails;
import com.toonpick.token.TokenService;
import com.toonpick.utils.CookieUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2UserDetails customUserDetails = (CustomOAuth2UserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        // 토큰 발급 및 쿠키 저장
        String refreshToken = tokenService.issueRefreshToken(username, role);
        response.addCookie(CookieUtils.createRefreshCookie(refreshToken));

        // 쿠키에서 redirect_uri 가져오기 (없으면 기본값 사용)
        String redirectUri = CookieUtils.getCookie(request, "redirect_uri")
                .map(Cookie::getValue)
                .orElse("null");

        logger.info("USER LOGIN SUCCESS (username-{})", username);
        logger.info("do send refresh token {}", redirectUri);

        // 인증 요청 정보 삭제
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        // 동적으로 redirect
        response.sendRedirect(redirectUri);
    }
}
