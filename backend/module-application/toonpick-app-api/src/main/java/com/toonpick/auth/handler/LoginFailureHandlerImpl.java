package com.toonpick.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toonpick.handler.LoginFailureHandler;
import com.toonpick.handler.LoginSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

@Component
public class LoginFailureHandlerImpl extends SimpleUrlAuthenticationSuccessHandler implements LoginFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginFailureHandlerImpl.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        logger.warn("Authentication failed: {}", exception.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "Authentication failed");
        responseBody.put("message", exception.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        response.getWriter().flush();
    }
}
