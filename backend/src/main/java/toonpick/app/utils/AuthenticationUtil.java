package toonpick.app.utils;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import toonpick.app.security.user.CustomUserDetails;

@Component
public class AuthenticationUtil {

    public String getUsernameFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated.");
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        return userDetails.getUsername();
    }

}
