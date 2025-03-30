package toonpick.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.dto.WebtoonReviewDTO;

import toonpick.dto.PagedResponseDTO;
import toonpick.service.WebtoonReviewService;

@Tag(name = "Webtoon Review", description = "웹툰 리뷰 관련 API (접근 권한 : Public)")
@RestController
@RequestMapping("/api/public/reviews")
@RequiredArgsConstructor
public class PublicWebtoonReviewController {

    private final WebtoonReviewService webtoonReviewService;

    @Operation(summary = "특정 리뷰 조회", description = "리뷰 ID를 사용하여 특정 리뷰를 조회합니다")
    @GetMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> getReview(
            @Parameter(description = "조회할 리뷰의 ID", required = true)
            @PathVariable Long reviewId
    ) {
        WebtoonReviewDTO reviewDTO = webtoonReviewService.getReview(reviewId);
        return reviewDTO != null ? ResponseEntity.ok(reviewDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "특정 웹툰의 리뷰 목록 조회", description = "특정 웹툰의 리뷰를 페이징하여 조회합니다")
    @GetMapping("/webtoon/{webtoonId}")
    public ResponseEntity<PagedResponseDTO<WebtoonReviewDTO>> getReviewsByWebtoon(
            @Parameter(description = "리뷰를 조회할 웹툰의 ID", required = true)
            @PathVariable Long webtoonId,
            @Parameter(description = "정렬 기준 (latest / rating)", required = false)
            @RequestParam(defaultValue = "latest") String sortBy,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", required = false)
            @RequestParam(defaultValue = "20") int size
    ) {
        PagedResponseDTO<WebtoonReviewDTO> pagedReviews = webtoonReviewService.getReviewsByWebtoon(webtoonId, sortBy, page, size);
        return ResponseEntity.ok(pagedReviews);
    }
}
