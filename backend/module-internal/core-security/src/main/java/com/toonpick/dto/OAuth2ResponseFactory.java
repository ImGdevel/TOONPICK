package com.toonpick.dto;

import com.toonpick.dto.oauth2responseImpl.GoogleResponse;
import com.toonpick.dto.oauth2responseImpl.NaverResponse;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuth2ResponseFactory {
    public static OAuth2Response of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleResponse(attributes);
            case "naver" -> new NaverResponse(attributes);
            default -> throw new OAuth2AuthenticationException("Unsupported OAuth Provider");
        };
    }
}
