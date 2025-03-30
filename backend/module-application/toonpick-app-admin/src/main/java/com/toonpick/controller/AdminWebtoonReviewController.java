package com.toonpick.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.toonpick.dto.WebtoonReviewDTO;
import com.toonpick.service.WebtoonReviewService;

@Tag(name = "Webtoon", description = "웹툰 리뷰 관련 API (접근 권한 : Admin)")
@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminWebtoonReviewController {

    private final WebtoonReviewService webtoonReviewService;

    // todo : Admin 사용 가능한 API
    // todo : 추후 @PreAuthorize("hasRole('ADMIN')") 어노테이션을 추가할 것

    @Operation(summary = "리뷰 삭제", description = "리뷰를 조회합니다 (관리자 권한)")
    @GetMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> getReview(
            @Parameter(description = "조회할 리뷰 id")
            @PathVariable Long reviewId
    ) {
        WebtoonReviewDTO reviewDTO = webtoonReviewService.getReview(reviewId);
        return reviewDTO != null ? ResponseEntity.ok(reviewDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "리뷰 삭제", description = "등록된 리뷰를 삭제합니다 (관리자 권한)")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "삭제할 리뷰 id")
            @PathVariable Long reviewId
    ) {
        webtoonReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
