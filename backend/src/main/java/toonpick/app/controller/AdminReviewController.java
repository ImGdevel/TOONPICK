package toonpick.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.service.WebtoonReviewService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final WebtoonReviewService webtoonReviewService;

    // todo : Admin 사용 가능한 API
    // todo : 추후 @PreAuthorize("hasRole('ADMIN')") 어노테이션을 추가할 것

    // 특정 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> getReview(@PathVariable Long reviewId) {
        WebtoonReviewDTO reviewDTO = webtoonReviewService.getReview(reviewId);
        return reviewDTO != null ? ResponseEntity.ok(reviewDTO) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 리뷰 강제 삭제 (관리자 권한)
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        webtoonReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
