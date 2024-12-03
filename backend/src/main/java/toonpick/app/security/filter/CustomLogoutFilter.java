
package toonpick.app.security.filter;

import com.nimbusds.openid.connect.sdk.validators.LogoutTokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import toonpick.app.security.handler.LogoutHandler;
import toonpick.app.security.jwt.JwtTokenValidator;


import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtTokenValidator tokenValidator;
    private final LogoutHandler logoutHandler;

    public CustomLogoutFilter(JwtTokenValidator tokenValidator, LogoutHandler logoutHandler) {
        this.tokenValidator = tokenValidator;
        this.logoutHandler = logoutHandler;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!isLogoutRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String refreshToken = tokenValidator.extractRefreshTokenFromCookies(httpRequest);
            tokenValidator.validateRefreshToken(refreshToken);
            logoutHandler.handleLogout(refreshToken, httpResponse);
        } catch (IllegalArgumentException e) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return "/logout".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod());
    }
}
