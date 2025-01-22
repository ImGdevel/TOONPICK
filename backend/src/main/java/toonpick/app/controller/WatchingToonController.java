package toonpick.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import toonpick.app.dto.webtoon.WebtoonResponseDTO;
import toonpick.app.service.WatchingToonsService;
import toonpick.app.utils.AuthenticationUtil;

import java.util.List;

@Tag(name = "Watching Toon", description = "회원 현재 감상 중 웹툰 (접근 권한 : Private)")
@RestController
@RequestMapping("/api/secure/watching")
@RequiredArgsConstructor
public class WatchingToonController {

    private final WatchingToonsService watchingToonsService;
    private final AuthenticationUtil authenticationUtil;

    @Operation(summary = "감상 웹툰 리스트에 추가", description = "회원의 감상 중 웹툰 리스트에 해당 웹툰을 추가합니다")
    @PostMapping("/{webtoonId}")
    public ResponseEntity<Boolean> addWatchingToon(
            @Parameter(description = "리스트에 추가할 웹툰 id")
            @PathVariable Long webtoonId,
            Authentication authentication
    ){
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean result = watchingToonsService.addWatchingToons(username, webtoonId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "감상 웹툰 제거", description = "회원의 감상 중 웹툰 리스트에 해당 웹툰을 제거합니다")
    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<Boolean> removeWatchingToon(
            @Parameter(description = "리스트에 추가할 웹툰 id")
            @PathVariable Long webtoonId,
            Authentication authentication
    ){
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        boolean result = watchingToonsService.removeWatchingToons(username, webtoonId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "감상 웹툰 리스트 조회", description = "현재 회원의 감상 중 웹툰 리스트를 조회합니다")
    @GetMapping
    public ResponseEntity<List<WebtoonResponseDTO>> getWatchingToons(Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<WebtoonResponseDTO> webtoons = watchingToonsService.getWebtoonsByUsername(username);
        return ResponseEntity.ok(webtoons);
    }

}
