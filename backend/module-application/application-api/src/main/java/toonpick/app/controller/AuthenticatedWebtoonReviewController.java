package toonpick.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import toonpick.utils.AuthenticationUtil;
import toonpick.dto.WebtoonReviewCreateDTO;
import toonpick.dto.WebtoonReviewDTO;
import toonpick.service.WebtoonReviewService;

import java.util.List;
import java.util.Optional;

@Tag(name = "Webtoon Review", description = "웹툰 리뷰 관련 API (접근 권한 : Private)")
@RestController
@RequestMapping("/api/secure/reviews")
@RequiredArgsConstructor
public class AuthenticatedWebtoonReviewController {

    private final WebtoonReviewService webtoonReviewService;
    private final AuthenticationUtil authenticationUtil;

    @Operation(summary = "새로운 리뷰 생성", description = "새로운 리뷰를 작성합니다")
    @PostMapping("/{webtoonId}")
    public ResponseEntity<WebtoonReviewDTO> createReview(
            @Parameter(description = "리뷰 작성 포맷", required = true)
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        WebtoonReviewDTO createdReview = webtoonReviewService.createReview(reviewCreateDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @Operation(summary = "기존 리뷰 수정", description = "기존에 작성항 리뷰를 수정합니다")
    @PutMapping("/{reviewId}")
    public ResponseEntity<WebtoonReviewDTO> updateReview(
            @Parameter(description = "수정할 리뷰 id")
            @PathVariable Long reviewId,
            @Parameter(description = "수정할 review 포맷")
            @RequestBody WebtoonReviewCreateDTO reviewCreateDTO,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication); // todo : 인증 여부만 확인
        WebtoonReviewDTO updatedReview = webtoonReviewService.updateReview(reviewId, reviewCreateDTO);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제/제거합니다")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "삭제할 리뷰 id")
            @PathVariable Long reviewId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication); // todo : 인증 여부만 확인
        webtoonReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "리뷰 좋아요 토글", description = "추천할 리뷰에 좋아요를 토글합니다")
    @ApiResponse(responseCode = "200", description = "기존에 추천이 없었다면 등록하고 true 반환하고, 이미 추천한 상태였다면 취소시키고 false 반환한다")
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> toggleLike(
            @Parameter(description = "좋아요를 토글할 리뷰 id")
            @PathVariable Long reviewId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        webtoonReviewService.toggleLike(username, reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰 조회", description = "회원이 작성한 좋아요를 조회합니다")
    @ApiResponse(responseCode = "200", description = "리뷰가 작성되어있는 경우 리뷰 반환")
    @GetMapping("/{webtoonId}/member")
    public ResponseEntity<WebtoonReviewDTO> getUserReviewForWebtoon(
            @Parameter(description = "조회할 웹툰 id")
            @PathVariable Long webtoonId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        Optional<WebtoonReviewDTO> reviewOpt = webtoonReviewService.getUserReviewForWebtoon(username, webtoonId); // todo : 왜 이렇게 만듬 ? 검증할 것
        return reviewOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "리뷰 추천 확인", description = "회원이 추천한 리뷰를 조회합니다")
    @GetMapping("/{webtoonId}/liked")
    public ResponseEntity<List<Long>> getLikedReviews(
            @Parameter(description = "조회할 웹툰")
            @PathVariable Long webtoonId,
            Authentication authentication) {
        String username = authenticationUtil.getUsernameFromAuthentication(authentication);
        List<Long> likedReviewIds = webtoonReviewService.getLikedReviewIds(username, webtoonId);
        return ResponseEntity.ok(likedReviewIds);
    }
}
