package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.utils.AuthenticationUtil;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.service.WebtoonReviewService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/secure/reviews")
public class AuthenticatedWebtoonReviewController {

    private final WebtoonReviewService webtoonReviewService;
    private final AuthenticationUtil authenticationUtil;

    // 리뷰 작성
    @PostMapping("/{webtoonId}")
    public ResponseEntity<WebtoonReviewDTO> createReview(
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        WebtoonReviewDTO createdReview = webtoonReviewService.createReview(reviewCreateDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication); // todo : 인증 여부만 확인
        WebtoonReviewDTO updatedReview = webtoonReviewService.updateReview(reviewId, reviewCreateDTO);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication); // todo : 인증 여부만 확인
        webtoonReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // 좋아요 토글
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long reviewId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        webtoonReviewService.toggleLike(username, reviewId);
        return ResponseEntity.ok().build();
    }

    // 사용자가 특정 웹툰에 작성한 리뷰 조회
    @GetMapping("/{webtoonId}/member")
    public ResponseEntity<WebtoonReviewDTO> getUserReviewForWebtoon(
            @PathVariable Long webtoonId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        Optional<WebtoonReviewDTO> reviewOpt = webtoonReviewService.getUserReviewForWebtoon(username, webtoonId);
        return reviewOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 사용자가 좋아요한 리뷰 ID 조회
    @GetMapping("/{webtoonId}/liked")
    public ResponseEntity<List<Long>> getLikedReviews(
            @PathVariable Long webtoonId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<Long> likedReviewIds = webtoonReviewService.getLikedReviewIds(username, webtoonId);
        return ResponseEntity.ok(likedReviewIds);
    }
}
