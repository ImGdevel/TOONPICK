package com.toonpick.constants;

public class SecurityConstants {
    public static final String[] PUBLIC_URLS = {
        "/", "/login", "/join", "/logout", "/reissue", "/actuator/**",
        "/oauth2/**", "/api/public/**", "/auth/**",
        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"
    };

    public static final String[] USER_URLS = {
        "/api/secure/**"
    };

    public static final String[] ADMIN_URLS = {
        "/admin"
    };
}
