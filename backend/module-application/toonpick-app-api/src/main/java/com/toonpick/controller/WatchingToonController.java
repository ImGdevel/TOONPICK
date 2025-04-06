package com.toonpick.controller;

import com.toonpick.annotation.CurrentUser;
import com.toonpick.dto.WebtoonResponseDTO;
import com.toonpick.service.WatchingToonsService;
import com.toonpick.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Watching Toon", description = "회원 현재 감상 중 웹툰 (접근 권한 : Private)")
@RestController
@RequestMapping("/api/secure/watching")
@RequiredArgsConstructor
public class WatchingToonController {

    private final WatchingToonsService watchingToonsService;

    @Operation(summary = "감상 웹툰 리스트에 추가", description = "회원의 감상 중 웹툰 리스트에 해당 웹툰을 추가합니다")
    @PostMapping("/{webtoonId}")
    public ResponseEntity<Boolean> addWatchingToon(
            @Parameter(description = "리스트에 추가할 웹툰 id")
            @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ){
        boolean result = watchingToonsService.addWatchingToons(user.getUsername(), webtoonId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "감상 웹툰 제거", description = "회원의 감상 중 웹툰 리스트에 해당 웹툰을 제거합니다")
    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Boolean> removeWatchingToon(
            @Parameter(description = "리스트에 추가할 웹툰 id")
            @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ){
        boolean result = watchingToonsService.removeWatchingToons(user.getUsername(), webtoonId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "감상 웹툰 리스트 조회", description = "현재 회원의 감상 중 웹툰 리스트를 조회합니다")
    @GetMapping
    public ResponseEntity<List<WebtoonResponseDTO>> getWatchingToons(
            @CurrentUser CustomUserDetails user
    ) {
        List<WebtoonResponseDTO> webtoons = watchingToonsService.getWebtoonsByUsername(user.getUsername());
        return ResponseEntity.ok(webtoons);
    }

}
