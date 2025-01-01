package toonpick.app.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import toonpick.app.auth.user.CustomUserDetails;
import toonpick.app.webtoon.dto.WebtoonDTO;
import toonpick.app.member.service.MemberFavoriteWebtoonService;

import java.util.List;

@RestController
@RequestMapping("/api/users/favorites")
public class MemberFavoriteWebtoonController {

    private final MemberFavoriteWebtoonService userFavoriteWebtoonService;

    @Autowired
    public MemberFavoriteWebtoonController(MemberFavoriteWebtoonService userFavoriteWebtoonService) {
        this.userFavoriteWebtoonService = userFavoriteWebtoonService;
    }

    // 선호 웹툰 추가
    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> addFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        userFavoriteWebtoonService.addFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Void> removeFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        userFavoriteWebtoonService.removeFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WebtoonDTO>> getFavoriteWebtoons(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        List<WebtoonDTO> favoriteWebtoons = userFavoriteWebtoonService.getFavoriteWebtoons(username);
        return ResponseEntity.ok(favoriteWebtoons);
    }

    @GetMapping("/{webtoonId}/is-favorite")
    public ResponseEntity<Boolean> isFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        boolean isFavorite = userFavoriteWebtoonService.isFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.ok(isFavorite);
    }
}
