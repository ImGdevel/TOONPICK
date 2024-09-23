package toonpick.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toonpick.app.dto.WebtoonDTO;
import toonpick.app.service.UserFavoriteWebtoonService;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/favorites")
public class UserFavoriteWebtoonController {

    private final UserFavoriteWebtoonService userFavoriteWebtoonService;

    @Autowired
    public UserFavoriteWebtoonController(UserFavoriteWebtoonService userFavoriteWebtoonService) {
        this.userFavoriteWebtoonService = userFavoriteWebtoonService;
    }

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> addFavoriteWebtoon(@PathVariable Long userId, @PathVariable Long webtoonId) {
        userFavoriteWebtoonService.addFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Void> removeFavoriteWebtoon(@PathVariable Long userId, @PathVariable Long webtoonId) {
        userFavoriteWebtoonService.removeFavoriteWebtoon(userId, webtoonId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WebtoonDTO>> getFavoriteWebtoons(@PathVariable Long userId) {
        List<WebtoonDTO> favoriteWebtoons = userFavoriteWebtoonService.getFavoriteWebtoons(userId);
        return ResponseEntity.ok(favoriteWebtoons);
    }
}
