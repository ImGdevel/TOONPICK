package toonpick.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toonpick.app.dto.CustomUserDetails;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.service.UserFavoriteWebtoonService;

import java.util.List;

@RestController
@RequestMapping("/api/users/favorites")
public class UserFavoriteWebtoonController {

    private final UserFavoriteWebtoonService userFavoriteWebtoonService;

    @Autowired
    public UserFavoriteWebtoonController(UserFavoriteWebtoonService userFavoriteWebtoonService) {
        this.userFavoriteWebtoonService = userFavoriteWebtoonService;
    }

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> addFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        userFavoriteWebtoonService.addFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Void> removeFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        userFavoriteWebtoonService.removeFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WebtoonDTO>> getFavoriteWebtoons(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        List<WebtoonDTO> favoriteWebtoons = userFavoriteWebtoonService.getFavoriteWebtoons(userId);
        return ResponseEntity.ok(favoriteWebtoons);
    }

    @GetMapping("/{webtoonId}/is-favorite")
    public ResponseEntity<Boolean> isFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        boolean isFavorite = userFavoriteWebtoonService.isFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok(isFavorite);
    }
}
