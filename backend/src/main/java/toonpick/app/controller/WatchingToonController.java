package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.service.WatchingToonsService;
import toonpick.app.utils.AuthenticationUtil;

import java.util.List;

@RestController
@RequestMapping("/api/secure/watching")
@RequiredArgsConstructor
public class WatchingToonController {

    private final WatchingToonsService watchingToonsService;
    private final AuthenticationUtil authenticationUtil;

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Boolean> addWatchingToon(@PathVariable Long webtoonId, Authentication authentication){
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean result = watchingToonsService.addWatchingToons(username, webtoonId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Boolean> removeWatchingToon(@PathVariable Long webtoonId, Authentication authentication){
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean result = watchingToonsService.removeWatchingToons(username, webtoonId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Webtoon>> getWatchingToons(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<Webtoon> webtoons = watchingToonsService.getWebtoonsByUsername(username);
        return ResponseEntity.ok(webtoons);
    }

}
