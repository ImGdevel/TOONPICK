package toonpick.app.security.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testCreateAccessTokenAndValidate() {
        // Given
        Long userId = 1L;
        String username = "testUser";
        String role = "USER";

        // When
        String token = jwtTokenProvider.createAccessToken(userId, username, role);

        // Then
        Assertions.assertNotNull(token);
        Assertions.assertEquals(userId, jwtTokenProvider.getUserId(token));
        Assertions.assertEquals(username, jwtTokenProvider.getUsername(token));
        Assertions.assertEquals(role, jwtTokenProvider.getRole(token));
        Assertions.assertFalse(jwtTokenProvider.isExpired(token));
    }
}
