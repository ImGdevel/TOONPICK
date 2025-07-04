package com.toonpick.internal.security.dto;

import com.toonpick.internal.security.dto.oauth2responseImpl.GoogleResponse;
import com.toonpick.internal.security.dto.oauth2responseImpl.NaverResponse;
import com.toonpick.common.type.ErrorCode;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

public class OAuth2ResponseFactory {
    public static OAuth2Response of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleResponse(attributes);
            case "naver" -> new NaverResponse(attributes);
            default -> throw new OAuth2AuthenticationException(ErrorCode.INVALID_OAUTH_PROVIDER.getMessage());
        };
    }
}
