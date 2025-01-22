package toonpick.app.utils;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import toonpick.app.exception.ErrorCode;
import toonpick.app.security.user.CustomUserDetails;

@Component
public class AuthenticationUtil {

    /**
     * Authentication에서 로그인 여부 검사 및 username 추출
     */
    public String getUsernameFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException(ErrorCode.USER_NOT_AUTHENTICATED.getMessage());
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new AuthenticationCredentialsNotFoundException(ErrorCode.USER_NOT_AUTHENTICATED.getMessage());
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        return userDetails.getUsername();
    }

}
