package toonpick.app.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import toonpick.app.entity.RefreshToken;
import toonpick.app.jwt.JwtUtil;
import toonpick.app.repository.RefreshTokenRepository;
import toonpick.app.service.AuthService;

import java.util.Date;

@Controller
@ResponseBody
public class ReissueController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public ReissueController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get token token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("token token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            System.out.println("invalid token token");
            //response status code
            return new ResponseEntity<>("invalid token token", HttpStatus.BAD_REQUEST);
        }

        try{
            String newAccess =  authService.refreshAccessToken(refresh);
            response.setHeader("access", newAccess);
        }
        catch (RuntimeException e){
            if(e.toString().equals("Invalid refresh token")){
                return new ResponseEntity<>("invalid token token", HttpStatus.BAD_REQUEST);
            }
        }

        //make new JWT

        //String newRefresh = jwtUtil.createRefreshToken(username, role);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        //refreshTokenRepository.deleteByToken(refresh);
        //addRefreshEntity(username, newRefresh, 86400000L);

        //response

        //response.addCookie(jwtUtil.createCookie("refresh", newRefresh));


        return new ResponseEntity<>(HttpStatus.OK);
    }

}