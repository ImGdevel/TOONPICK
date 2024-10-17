package toonpick.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import toonpick.app.dto.CustomUserDetails;
import toonpick.app.dto.PagedResponseDTO;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.service.WebtoonReviewService;

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

    @PostMapping
    public ResponseEntity<WebtoonReviewDTO> createReview(
            @PathVariable Long webtoonId,
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        WebtoonReviewDTO createdReview = webtoonReviewService.createReview(reviewCreateDTO,webtoonId, userId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> getReview(@PathVariable Long reviewId) {
        WebtoonReviewDTO reviewDTO = webtoonReviewService.getReview(reviewId);
        return reviewDTO != null ? ResponseEntity.ok(reviewDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO) {
        WebtoonReviewDTO updatedReview = webtoonReviewService.updateReview(reviewId, reviewCreateDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        webtoonReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long reviewId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        CompletableFuture<Boolean> likeResult = webtoonReviewService.toggleLike(userId, reviewId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("liked", likeResult.join());

        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<PagedResponseDTO<WebtoonReviewDTO>> getReviewsByWebtoon(
            @PathVariable Long webtoonId,
            @RequestParam(defaultValue = "latest") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PagedResponseDTO<WebtoonReviewDTO> pagedReviews = webtoonReviewService.getReviewsByWebtoon(webtoonId, sortBy, page, size);
        return ResponseEntity.ok(pagedReviews);
    }

    @GetMapping("/users")
    public ResponseEntity<WebtoonReviewDTO> getUserReviewForWebtoon(
            @PathVariable Long webtoonId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        Optional<WebtoonReviewDTO> reviewOpt = webtoonReviewService.getUserReviewForWebtoon(userId, webtoonId);
        return reviewOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/liked-reviews")
    public ResponseEntity<List<Long>> getLikedReviews(
            @PathVariable Long webtoonId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        List<Long> likedReviewIds = webtoonReviewService.getLikedReviewIds(userId, webtoonId);
        return ResponseEntity.ok(likedReviewIds);
    }

}
