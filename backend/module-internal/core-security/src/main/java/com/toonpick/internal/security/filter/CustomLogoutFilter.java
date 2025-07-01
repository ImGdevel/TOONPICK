
package com.toonpick.internal.security.filter;

import com.toonpick.internal.security.handler.LogoutHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final LogoutHandler logoutHandler;

    private final Logger logger = LoggerFactory.getLogger(CustomLogoutFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isLogoutRequest(httpRequest)) {
            logoutHandler.handleLogout(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }


    private boolean isLogoutRequest(HttpServletRequest request) {
        return "/logout".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod());
    }
}
