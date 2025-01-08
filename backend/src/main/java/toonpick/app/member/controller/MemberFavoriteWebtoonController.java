package toonpick.app.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.common.utils.AuthenticationUtil;
import toonpick.app.member.service.MemberFavoriteWebtoonService;
import toonpick.app.webtoon.dto.WebtoonDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/secure/users/favorites")
public class MemberFavoriteWebtoonController {

    private final MemberFavoriteWebtoonService memberFavoriteWebtoonService;
    private final AuthenticationUtil authenticationUtil;

    // 즐겨찾기 추가
    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> addFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberFavoriteWebtoonService.addFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Void> removeFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        memberFavoriteWebtoonService.removeFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 즐겨찾기 목록 조회
    @GetMapping
    public ResponseEntity<List<WebtoonDTO>> getFavoriteWebtoons(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<WebtoonDTO> favoriteWebtoons = memberFavoriteWebtoonService.getFavoriteWebtoons(username);
        return ResponseEntity.ok(favoriteWebtoons);
    }

    // 특정 웹툰이 즐겨찾기인지 확인
    @GetMapping("/{webtoonId}/is-favorite")
    public ResponseEntity<Boolean> isFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean isFavorite = memberFavoriteWebtoonService.isFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.ok(isFavorite);
    }
}
