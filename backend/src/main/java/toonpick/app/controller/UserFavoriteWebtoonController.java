package toonpick.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.jwt.JwtTokenProvider;
import toonpick.app.service.UserFavoriteWebtoonService;

import java.util.List;

@RestController
@RequestMapping("/api/users/favorites")
public class UserFavoriteWebtoonController {

    private final UserFavoriteWebtoonService userFavoriteWebtoonService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserFavoriteWebtoonController(UserFavoriteWebtoonService userFavoriteWebtoonService, JwtTokenProvider jwtTokenProvider) {
        this.userFavoriteWebtoonService = userFavoriteWebtoonService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Method to extract userId from the AccessToken
    private Long extractUserIdFromToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return jwtTokenProvider.getUserId(token);
    }

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> addFavoriteWebtoon(@PathVariable Long webtoonId, HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        userFavoriteWebtoonService.addFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Void> removeFavoriteWebtoon(@PathVariable Long webtoonId, HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        userFavoriteWebtoonService.removeFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WebtoonDTO>> getFavoriteWebtoons(HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        List<WebtoonDTO> favoriteWebtoons = userFavoriteWebtoonService.getFavoriteWebtoons(userId);
        return ResponseEntity.ok(favoriteWebtoons);
    }

    @GetMapping("/{webtoonId}/is-favorite")
    public ResponseEntity<Boolean> isFavoriteWebtoon(@PathVariable Long webtoonId, HttpServletRequest request) {
        Long userId = extractUserIdFromToken(request);
        boolean isFavorite = userFavoriteWebtoonService.isFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok(isFavorite);
    }

}
