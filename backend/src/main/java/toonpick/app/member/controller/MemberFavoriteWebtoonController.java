package toonpick.app.member.controller;

import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/users/favorites")
public class MemberFavoriteWebtoonController {

    private final MemberFavoriteWebtoonService userFavoriteWebtoonService;
    private final AuthenticationUtil authenticationUtil;

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Void> addFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        userFavoriteWebtoonService.addFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Void> removeFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        userFavoriteWebtoonService.removeFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WebtoonDTO>> getFavoriteWebtoons(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<WebtoonDTO> favoriteWebtoons = userFavoriteWebtoonService.getFavoriteWebtoons(username);
        return ResponseEntity.ok(favoriteWebtoons);
    }

    @GetMapping("/{webtoonId}/is-favorite")
    public ResponseEntity<Boolean> isFavoriteWebtoon(@PathVariable Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean isFavorite = userFavoriteWebtoonService.isFavoriteWebtoon(username, webtoonId);
        return ResponseEntity.ok(isFavorite);
    }
}
