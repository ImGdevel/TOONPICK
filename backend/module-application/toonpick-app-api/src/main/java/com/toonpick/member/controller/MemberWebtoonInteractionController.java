package com.toonpick.member.controller;

import com.toonpick.internal.security.annotation.CurrentUser;
import com.toonpick.domain.member.enums.WatchingStatus;
import com.toonpick.member.request.LastReadUpdateRequest;
import com.toonpick.member.request.NotificationUpdateRequest;
import com.toonpick.member.request.WebtoonInteractionResponse;
import com.toonpick.member.service.MemberWebtoonInteractionService;
import com.toonpick.internal.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Webtoon Interaction", description = "웹툰 상호작용 관련 API (접근 권한 : Private)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/secure/webtoons")
public class MemberWebtoonInteractionController {

    private final MemberWebtoonInteractionService interactionService;

    @Operation(summary = "웹툰 좋아요 등록", description = "회원이 해당 웹툰에 좋아요를 등록합니다.")
    @PostMapping("/{webtoonId}/like")
    public ResponseEntity<Void> like(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        interactionService.markAsLiked(webtoonId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "웹툰 좋아요 취소", description = "회원이 해당 웹툰의 좋아요를 취소합니다.")
    @DeleteMapping("/{webtoonId}/like")
    public ResponseEntity<Void> unlike(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        interactionService.cancelLike(webtoonId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "웹툰 북마크 등록", description = "회원이 해당 웹툰을 북마크합니다.")
    @PostMapping("/{webtoonId}/bookmark")
    public ResponseEntity<Void> bookmark(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        interactionService.addToBookmarks(webtoonId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "웹툰 북마크 취소", description = "회원이 해당 웹툰의 북마크를 취소합니다.")
    @DeleteMapping("/{webtoonId}/bookmark")
    public ResponseEntity<Void> unbookmark(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        interactionService.removeFromBookmarks(webtoonId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "시청 상태 변경", description = "회원이 웹툰의 시청 상태를 변경합니다")
    @PatchMapping("/{webtoonId}/status")
    public ResponseEntity<Void> updateWatchingStatus(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @RequestBody WatchingStatus request,
            @CurrentUser CustomUserDetails user
    ) {
        interactionService.changeWatchingStatus(webtoonId, request, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "마지막 읽은 회차 갱신", description = "회원이 마지막으로 읽은 회차 및 읽은 시각을 갱신합니다")
    @PatchMapping("/{webtoonId}/last-read")
    public ResponseEntity<Void> updateLastRead(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @RequestBody LastReadUpdateRequest request,
            @CurrentUser CustomUserDetails user
    ) {
        interactionService.updateLastReadingProgress(webtoonId, request, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림 설정 변경", description = "회원이 웹툰 알림 수신 여부를 설정합니다")
    @PatchMapping("/{webtoonId}/notification")
    public ResponseEntity<Void> updateNotification(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @RequestBody NotificationUpdateRequest request,
            @CurrentUser CustomUserDetails user
    ) {

        interactionService.updateNotificationSetting(webtoonId, request.isEnabled(), user.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "웹툰 상호작용 정보 조회", description = "회원이 특정 웹툰에 대해 기록한 상호작용 정보를 조회합니다")
    @GetMapping("/{webtoonId}/interaction")
    public ResponseEntity<WebtoonInteractionResponse> getInteraction(
            @Parameter(description = "웹툰 ID") @PathVariable Long webtoonId,
            @CurrentUser CustomUserDetails user
    ) {
        return ResponseEntity.ok(interactionService.getInteraction(webtoonId, user.getUsername()));
    }
}
