package toonpick.app.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.review.dto.WebtoonReviewDTO;
import toonpick.app.review.service.WebtoonReviewService;
import toonpick.app.webtoon.dto.PagedResponseDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/reviews")
public class PublicWebtoonReviewController {

    private final WebtoonReviewService webtoonReviewService;

    // 특정 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> getReview(@PathVariable Long reviewId) {
        WebtoonReviewDTO reviewDTO = webtoonReviewService.getReview(reviewId);
        return reviewDTO != null ? ResponseEntity.ok(reviewDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 특정 웹툰의 리뷰들 가져오기
    @GetMapping("/{webtoonId}")
    public ResponseEntity<PagedResponseDTO<WebtoonReviewDTO>> getReviewsByWebtoon(
            @PathVariable Long webtoonId,
            @RequestParam(defaultValue = "latest") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PagedResponseDTO<WebtoonReviewDTO> pagedReviews = webtoonReviewService.getReviewsByWebtoon(webtoonId, sortBy, page, size);
        return ResponseEntity.ok(pagedReviews);
    }
}
