package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.service.BookmarkToonsService;
import toonpick.app.utils.AuthenticationUtil;

import java.util.List;

@RestController
@RequestMapping("/api/secure/bookmarks")
@RequiredArgsConstructor
public class BookmarkToonController {

    private final BookmarkToonsService bookmarkToonsService;
    private final AuthenticationUtil authenticationUtil;

    @PostMapping("/{webtoonId}")
    public ResponseEntity<Boolean> addBookmark(@RequestParam Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean result = bookmarkToonsService.addBookmarkToons(username, webtoonId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Boolean> removeBookmark(@RequestParam Long webtoonId, Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean result = bookmarkToonsService.removeBookmarkToons(username, webtoonId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<Webtoon>> getBookmarks(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<Webtoon> webtoons = bookmarkToonsService.getWebtoonsByUsername(username);
        return ResponseEntity.ok(webtoons);
    }
}
