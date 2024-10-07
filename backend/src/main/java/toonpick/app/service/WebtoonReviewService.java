package toonpick.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.User;
import toonpick.app.entity.WebtoonReview;
import toonpick.app.entity.Webtoon;
import toonpick.app.mapper.WebtoonReviewMapper;
import toonpick.app.repository.WebtoonReviewRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebtoonReviewService {

    private final WebtoonReviewRepository webtoonReviewRepository;
    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonReviewMapper userRatingMapper;

    @Autowired
    public WebtoonReviewService(WebtoonReviewRepository webtoonReviewRepository, UserRepository userRepository, WebtoonRepository webtoonRepository, WebtoonReviewMapper userRatingMapper) {
        this.webtoonReviewRepository = webtoonReviewRepository;
        this.userRepository = userRepository;
        this.webtoonRepository = webtoonRepository;
        this.userRatingMapper = userRatingMapper;
    }

    public WebtoonReviewDTO createUserRating(Long userId, Long webtoonId, float rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new IllegalArgumentException("Webtoon not found"));

        WebtoonReview userRating = WebtoonReview.builder()
                .user(user)
                .webtoon(webtoon)
                .rating(rating)
                .comment(comment)
                .likes(0)
                .modifyDate(LocalDate.now())
                .build();

        WebtoonReview savedUserRating = webtoonReviewRepository.save(userRating);

        return userRatingMapper.toDTO(savedUserRating);
    }

    public WebtoonReviewDTO updateUserRating(Long ratingId, float rating, String comment, int likes) {
        WebtoonReview userRating = webtoonReviewRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        userRating.update(rating, comment, LocalDate.now());
        WebtoonReview updatedUserRating = webtoonReviewRepository.save(userRating);

        return userRatingMapper.toDTO(updatedUserRating);
    }

    public Page<WebtoonReviewDTO> getRatingsByLikes(Long webtoonId, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("likes").descending());
        Page<WebtoonReview> userRatings = webtoonReviewRepository.findByWebtoonIdOrderByLikesDesc(webtoonId, pageable);

        return userRatings.map(userRatingMapper::toDTO);
    }

    public Page<WebtoonReviewDTO> getRatingsByLatest(Long webtoonId, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("modifyDate").descending());
        Page<WebtoonReview> userRatings = webtoonReviewRepository.findByWebtoonIdOrderByModifyDateDesc(webtoonId, pageable);

        return userRatings.map(userRatingMapper::toDTO);
    }

    public List<WebtoonReviewDTO> getRatingsForWebtoonAll(Long webtoonId) {
        List<WebtoonReview> userRatings = webtoonReviewRepository.findByWebtoonId(webtoonId);

        return userRatings.stream()
                .map(userRatingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void updateLikes(Long ratingId, int likes) {
        WebtoonReview userRating = webtoonReviewRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));
        userRating.updateLikes(likes);
        webtoonReviewRepository.save(userRating);
    }

    public List<WebtoonReviewDTO> getRatingsByUser(Long userId) {
        List<WebtoonReview> userRatings = webtoonReviewRepository.findByUserId(userId);

        return userRatings.stream()
                .map(userRatingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteUserRating(Long ratingId) {
        webtoonReviewRepository.deleteById(ratingId);
    }
}
