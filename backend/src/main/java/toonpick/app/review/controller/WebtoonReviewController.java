package toonpick.app.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import toonpick.app.common.utils.AuthenticationUtil;
import toonpick.app.webtoon.dto.PagedResponseDTO;
import toonpick.app.review.dto.WebtoonReviewCreateDTO;
import toonpick.app.review.dto.WebtoonReviewDTO;
import toonpick.app.review.service.WebtoonReviewService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/webtoon/{webtoonId}/reviews")
@RequiredArgsConstructor
public class WebtoonReviewController {

    private final WebtoonReviewService webtoonReviewService;
    private final AuthenticationUtil authenticationUtil;

    // 리뷰 작성
    @PostMapping
    public ResponseEntity<WebtoonReviewDTO> createReview(
            @PathVariable Long webtoonId,
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        WebtoonReviewDTO createdReview = webtoonReviewService.createReview(reviewCreateDTO, webtoonId, username);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    // 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> getReview(@PathVariable Long reviewId) {
        WebtoonReviewDTO reviewDTO = webtoonReviewService.getReview(reviewId);
        return reviewDTO != null ? ResponseEntity.ok(reviewDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 리뷰 수정하기
    @PutMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO) {
        WebtoonReviewDTO updatedReview = webtoonReviewService.updateReview(reviewId, reviewCreateDTO);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        webtoonReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // 리뷰에 좋아요 토글
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long reviewId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        CompletableFuture<Boolean> likeResult = webtoonReviewService.toggleLike(username, reviewId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("liked", likeResult.join());

        return ResponseEntity.ok(response);
    }

    // 웹툰에 해당되는 리뷰들 가져오기 (페이지 조회)
    @GetMapping
    public ResponseEntity<PagedResponseDTO<WebtoonReviewDTO>> getReviewsByWebtoon(
            @PathVariable Long webtoonId,
            @RequestParam(defaultValue = "latest") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PagedResponseDTO<WebtoonReviewDTO> pagedReviews = webtoonReviewService.getReviewsByWebtoon(webtoonId, sortBy, page, size);
        return ResponseEntity.ok(pagedReviews);
    }

    // User가 작성한
    @GetMapping("/users")
    public ResponseEntity<WebtoonReviewDTO> getUserReviewForWebtoon(
            @PathVariable Long webtoonId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        Optional<WebtoonReviewDTO> reviewOpt = webtoonReviewService.getUserReviewForWebtoon(username, webtoonId);
        return reviewOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/liked-reviews")
    public ResponseEntity<List<Long>> getLikedReviews(
            @PathVariable Long webtoonId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);

        List<Long> likedReviewIds = webtoonReviewService.getLikedReviewIds(username, webtoonId);
        return ResponseEntity.ok(likedReviewIds);
    }

}
