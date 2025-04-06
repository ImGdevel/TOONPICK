package com.toonpick.member_enagement.controller;

import com.toonpick.annotation.CurrentUser;
import com.toonpick.webtoon.response.WebtoonResponseDTO;
import com.toonpick.member_enagement.service.BookmarkToonsService;
import com.toonpick.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Bookmark Toon", description = "회원 북마크 웹툰 (접근 권한 : Private)")
@RestController
@RequestMapping("/api/secure/bookmarks")
@RequiredArgsConstructor
public class BookmarkToonController {

    private final BookmarkToonsService bookmarkToonsService;

    @Operation(summary = "북마크 웹툰 추가", description = "회원의 북마크 웹툰 리스트에 해당 웹툰을 추가합니다")
    @PostMapping("/{webtoonId}")
    public ResponseEntity<Boolean> addBookmark(
            @Parameter(description = "리스트에 추가할 웹툰 id")
            @RequestParam Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        boolean result = bookmarkToonsService.addBookmarkToons(user.getUsername(), webtoonId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "북마크 웹툰 제거", description = "회원의 북마크 웹툰 리스트에 해당 웹툰을 제거합니다")
    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Boolean> removeBookmark(
            @Parameter(description = "리스트에 제거할 웹툰 id")
            @RequestParam Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        boolean result = bookmarkToonsService.removeBookmarkToons(user.getUsername(), webtoonId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "북마크 웹툰 리스트 조회", description = "현재 회원의 북마크 웹툰 리스트를 조회합니다")
    @GetMapping
    public ResponseEntity<List<WebtoonResponseDTO>> getBookmarks(
            @CurrentUser CustomUserDetails user
    ) {
        List<WebtoonResponseDTO> webtoons = bookmarkToonsService.getWebtoonsByUsername(user.getUsername());
        return ResponseEntity.ok(webtoons);
    }
}
