package toonpick.app.unit.utils;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import toonpick.utils.CookieUtils;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
class CookieUtilsTest {

    @BeforeEach
    void setUp() {
        // @Value로 주입되는 refreshTokenExpiration 값 설정
        ReflectionTestUtils.setField(CookieUtils.class, "refreshTokenExpiration", 3600);
    }

    @DisplayName("리프레시 토큰 쿠키 생성 테스트")
    @Test
    void testCreateRefreshCookie() {
        // given
        String token = "sample_refresh_token";

        // when
        Cookie cookie = CookieUtils.createRefreshCookie(token);

        // then
        assertNotNull(cookie);
        assertEquals("refresh", cookie.getName());
        assertEquals(token, cookie.getValue());
        assertEquals(3600, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @DisplayName("빈 쿠키 생성 테스트")
    @Test
    void testCreateEmptyCookie() {
        // given
        String key = "refresh";

        // when
        Cookie cookie = CookieUtils.createEmptyCookie(key);

        // then
        assertNotNull(cookie);
        assertEquals(key, cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals(0, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @DisplayName("일반 쿠키 생성 테스트")
    @Test
    void testCreateCookie() {
        // given
        String key = "testKey";
        String value = "testValue";
        int expiration = 120;

        // when
        Cookie cookie = CookieUtils.createCookie(key, value, expiration);

        // then
        assertNotNull(cookie);
        assertEquals(key, cookie.getName());
        assertEquals(value, cookie.getValue());
        assertEquals(expiration, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }
}
