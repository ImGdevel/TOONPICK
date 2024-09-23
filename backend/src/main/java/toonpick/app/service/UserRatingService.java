package toonpick.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toonpick.app.entity.User;
import toonpick.app.entity.UserRating;
import toonpick.app.entity.Webtoon;
import toonpick.app.repository.UserRatingRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserRatingService {

    private final UserRatingRepository userRatingRepository;
    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;

    @Autowired
    public UserRatingService(UserRatingRepository userRatingRepository, UserRepository userRepository, WebtoonRepository webtoonRepository) {
        this.userRatingRepository = userRatingRepository;
        this.userRepository = userRepository;
        this.webtoonRepository = webtoonRepository;
    }

    public UserRating createUserRating(Long userId, Long webtoonId, float rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new IllegalArgumentException("Webtoon not found"));

        UserRating userRating = UserRating.builder()
                .user(user)
                .webtoon(webtoon)
                .reviewDate(LocalDate.now())
                .rating(rating)
                .comment(comment)
                .likes(0)
                .build();

        return userRatingRepository.save(userRating);
    }

    public UserRating updateUserRating(Long ratingId, float rating, String comment, int likes) {
        UserRating userRating = userRatingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        userRating.update(LocalDate.now(), rating, comment, likes);
        return userRatingRepository.save(userRating);
    }

    public List<UserRating> getRatingsForWebtoon(Long webtoonId) {
        return userRatingRepository.findByWebtoonId(webtoonId);
    }

    public List<UserRating> getRatingsByUser(Long userId) {
        return userRatingRepository.findByUserId(userId);
    }

    public void deleteUserRating(Long ratingId) {
        userRatingRepository.deleteById(ratingId);
    }
}
