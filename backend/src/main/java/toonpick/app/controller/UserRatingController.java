package toonpick.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import toonpick.app.entity.UserRating;
import toonpick.app.service.UserRatingService;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class UserRatingController {

    private final UserRatingService userRatingService;

    @Autowired
    public UserRatingController(UserRatingService userRatingService) {
        this.userRatingService = userRatingService;
    }

    // API to create a new rating with comment
    @PostMapping
    public ResponseEntity<UserRating> createUserRating(
            @RequestParam Long userId,
            @RequestParam Long webtoonId,
            @RequestParam float rating,
            @RequestParam String comment) {
        UserRating userRating = userRatingService.createUserRating(userId, webtoonId, rating, comment);
        return new ResponseEntity<>(userRating, HttpStatus.CREATED);
    }

    // API to update a rating
    @PutMapping("/{ratingId}")
    public ResponseEntity<UserRating> updateUserRating(
            @PathVariable Long ratingId,
            @RequestParam float rating,
            @RequestParam String comment,
            @RequestParam int likes) {
        UserRating updatedRating = userRatingService.updateUserRating(ratingId, rating, comment, likes);
        return new ResponseEntity<>(updatedRating, HttpStatus.OK);
    }

    // API to get all reviews for a specific webtoon
    @GetMapping("/webtoon/{webtoonId}")
    public ResponseEntity<List<UserRating>> getRatingsForWebtoon(@PathVariable Long webtoonId) {
        List<UserRating> ratings = userRatingService.getRatingsForWebtoon(webtoonId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    // API to get all reviews made by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRating>> getRatingsByUser(@PathVariable Long userId) {
        List<UserRating> ratings = userRatingService.getRatingsByUser(userId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    // API to delete a rating
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteUserRating(@PathVariable Long ratingId) {
        userRatingService.deleteUserRating(ratingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
