package com.toonpick.controller;

import com.toonpick.annotation.CurrentUser;
import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.service.FavoriteToonService;
import com.toonpick.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/secure/member/favorites")
public class FavoriteToonController {

    private final FavoriteToonService favoriteToonService;

    // 즐겨찾기 추가
    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> addFavoriteWebtoon(
            @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        favoriteToonService.addFavoriteWebtoon(user.getUsername(), webtoonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Void> removeFavoriteWebtoon(
            @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        favoriteToonService.removeFavoriteWebtoon(user.getUsername(), webtoonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 즐겨찾기 목록 조회
    @GetMapping
    public ResponseEntity<List<WebtoonResponseDTO>> getFavoriteWebtoons(
            @CurrentUser CustomUserDetails user
    ) {
        List<WebtoonResponseDTO> favoriteWebtoons = favoriteToonService.getFavoriteWebtoons(user.getUsername());
        return ResponseEntity.ok(favoriteWebtoons);
    }

    // 특정 웹툰이 즐겨찾기인지 확인
    @GetMapping("/{webtoonId}/is-favorite")
    public ResponseEntity<Boolean> isFavoriteWebtoon(
            @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        boolean isFavorite = favoriteToonService.isFavoriteWebtoon(user.getUsername(), webtoonId);
        return ResponseEntity.ok(isFavorite);
    }
}
