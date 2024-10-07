package toonpick.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.dto.CustomUserDetails;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.service.WebtoonReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/user-ratings")
public class WebtoonReviewController {

    private final WebtoonReviewService webtoonReviewService;

    @Autowired
    public WebtoonReviewController(WebtoonReviewService webtoonReviewService) {
        this.webtoonReviewService = webtoonReviewService;
    }

    // API to create a new rating with comment
    @PostMapping
    public ResponseEntity<WebtoonReviewDTO> createUserRating(
            @RequestParam Long webtoonId,
            @RequestParam float rating,
            @RequestParam String comment,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        WebtoonReviewDTO webtoonReviewDTO = webtoonReviewService.createUserRating(userId, webtoonId, rating, comment);
        return ResponseEntity.ok(webtoonReviewDTO);
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<WebtoonReviewDTO> updateUserRating(
            @PathVariable Long ratingId,
            @RequestParam float rating,
            @RequestParam String comment,
            @RequestParam int likes) {

        WebtoonReviewDTO updatedWebtoonReviewDTO = webtoonReviewService.updateUserRating(ratingId, rating, comment, likes);
        return ResponseEntity.ok(updatedWebtoonReviewDTO);
    }

    @GetMapping("/webtoon/{webtoonId}/likes")
    public ResponseEntity<Page<WebtoonReviewDTO>> getRatingsByLikes(
            @PathVariable Long webtoonId,
            @RequestParam int page) {

        Page<WebtoonReviewDTO> userRatings = webtoonReviewService.getRatingsByLikes(webtoonId, page);
        return ResponseEntity.ok(userRatings);
    }

    @GetMapping("/webtoon/{webtoonId}/latest")
    public ResponseEntity<Page<WebtoonReviewDTO>> getRatingsByLatest(
            @PathVariable Long webtoonId,
            @RequestParam int page) {

        Page<WebtoonReviewDTO> userRatings = webtoonReviewService.getRatingsByLatest(webtoonId, page);
        return ResponseEntity.ok(userRatings);
    }

    @GetMapping("/webtoon/{webtoonId}")
    public ResponseEntity<List<WebtoonReviewDTO>> getRatingsForWebtoonAll(@PathVariable Long webtoonId) {
        List<WebtoonReviewDTO> userRatings = webtoonReviewService.getRatingsForWebtoonAll(webtoonId);
        return ResponseEntity.ok(userRatings);
    }

    @GetMapping("/user")
    public ResponseEntity<List<WebtoonReviewDTO>> getRatingsByUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        List<WebtoonReviewDTO> userRatings = webtoonReviewService.getRatingsByUser(userId);
        return ResponseEntity.ok(userRatings);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteUserRating(@PathVariable Long ratingId) {
        webtoonReviewService.deleteUserRating(ratingId);
        return ResponseEntity.noContent().build();
    }
}
