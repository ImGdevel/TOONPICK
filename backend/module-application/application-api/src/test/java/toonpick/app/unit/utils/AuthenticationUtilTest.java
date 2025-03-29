package toonpick.app.unit.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import toonpick.app.security.user.CustomUserDetails;
import toonpick.app.utils.AuthenticationUtil;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("UnitTest")
class AuthenticationUtilTest {

    private final AuthenticationUtil authenticationUtil = new AuthenticationUtil();

    @DisplayName("Authentication에서 사용자 이름 정상 추출 테스트")
    @Test
    void testGetUsernameFromAuthentication_Success() {
        // given
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        // when
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        // then
        assertNotNull(username);
        assertEquals("testUser", username);
        verify(authentication, times(1)).getPrincipal();
        verify(userDetails, times(1)).getUsername();
    }

    @DisplayName("Authentication이 null일 때 예외 테스트")
    @Test
    void testGetUsernameFromAuthentication_NullAuthentication() {
        // when & then
        AuthenticationCredentialsNotFoundException exception = assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> authenticationUtil.getUsernameFromAuthentication(null)
        );

        assertEquals("User is not authenticated", exception.getMessage());
    }

    @DisplayName("Authentication의 Principal이 올바르지 않을 때 예외 테스트")
    @Test
    void testGetUsernameFromAuthentication_InvalidPrincipal() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("invalidPrincipal");

        // when & then
        AuthenticationCredentialsNotFoundException exception = assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> authenticationUtil.getUsernameFromAuthentication(authentication)
        );

        assertEquals("User is not authenticated", exception.getMessage());
        verify(authentication, times(1)).getPrincipal();
    }
}
