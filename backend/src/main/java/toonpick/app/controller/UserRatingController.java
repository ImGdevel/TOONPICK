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
import toonpick.app.dto.UserRatingDTO;
import toonpick.app.service.UserRatingService;

import java.util.List;

@RestController
@RequestMapping("/api/user-ratings")
public class UserRatingController {

    private final UserRatingService userRatingService;

    @Autowired
    public UserRatingController(UserRatingService userRatingService) {
        this.userRatingService = userRatingService;
    }

    // API to create a new rating with comment
    @PostMapping
    public ResponseEntity<UserRatingDTO> createUserRating(
            @RequestParam Long webtoonId,
            @RequestParam float rating,
            @RequestParam String comment,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        UserRatingDTO userRatingDTO = userRatingService.createUserRating(userId, webtoonId, rating, comment);
        return ResponseEntity.ok(userRatingDTO);
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<UserRatingDTO> updateUserRating(
            @PathVariable Long ratingId,
            @RequestParam float rating,
            @RequestParam String comment,
            @RequestParam int likes) {

        UserRatingDTO updatedUserRatingDTO = userRatingService.updateUserRating(ratingId, rating, comment, likes);
        return ResponseEntity.ok(updatedUserRatingDTO);
    }

    @GetMapping("/webtoon/{webtoonId}/likes")
    public ResponseEntity<Page<UserRatingDTO>> getRatingsByLikes(
            @PathVariable Long webtoonId,
            @RequestParam int page) {

        Page<UserRatingDTO> userRatings = userRatingService.getRatingsByLikes(webtoonId, page);
        return ResponseEntity.ok(userRatings);
    }

    @GetMapping("/webtoon/{webtoonId}/latest")
    public ResponseEntity<Page<UserRatingDTO>> getRatingsByLatest(
            @PathVariable Long webtoonId,
            @RequestParam int page) {

        Page<UserRatingDTO> userRatings = userRatingService.getRatingsByLatest(webtoonId, page);
        return ResponseEntity.ok(userRatings);
    }

    @GetMapping("/webtoon/{webtoonId}")
    public ResponseEntity<List<UserRatingDTO>> getRatingsForWebtoonAll(@PathVariable Long webtoonId) {
        List<UserRatingDTO> userRatings = userRatingService.getRatingsForWebtoonAll(webtoonId);
        return ResponseEntity.ok(userRatings);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserRatingDTO>> getRatingsByUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        List<UserRatingDTO> userRatings = userRatingService.getRatingsByUser(userId);
        return ResponseEntity.ok(userRatings);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteUserRating(@PathVariable Long ratingId) {
        userRatingService.deleteUserRating(ratingId);
        return ResponseEntity.noContent().build();
    }
}
