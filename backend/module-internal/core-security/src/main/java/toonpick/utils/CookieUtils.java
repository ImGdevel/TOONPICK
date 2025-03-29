package toonpick.utils;


import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    private static int refreshTokenExpiration;

    @Autowired
    public void setRefreshTokenExpiration(@Value("${spring.jwt.refresh-token-expiration}") int expiration) {
        CookieUtils.refreshTokenExpiration = expiration;
    }

    /**
     * 리프레시 토큰 쿠키 생성
     */
    public static Cookie createRefreshCookie(String token) {
        return createCookie("refresh", token, refreshTokenExpiration);
    }

    /**
     * 빈 쿠키 생성 (쿠키 삭제용)
     */
    public static Cookie createEmptyCookie(String key) {
        return createCookie(key, "", 0);
    }

    /**
     * 쿠키 생성 공통 메서드
     */
    public static Cookie createCookie(String key, String value, int cookieExpiration) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(cookieExpiration);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
