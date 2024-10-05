package toonpick.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import toonpick.app.dto.UserRatingDTO;
import toonpick.app.entity.User;
import toonpick.app.entity.UserRating;
import toonpick.app.entity.Webtoon;
import toonpick.app.mapper.UserRatingMapper;
import toonpick.app.repository.UserRatingRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRatingService {

    private final UserRatingRepository userRatingRepository;
    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;
    private final UserRatingMapper userRatingMapper;  // Mapper 추가

    @Autowired
    public UserRatingService(UserRatingRepository userRatingRepository, UserRepository userRepository, WebtoonRepository webtoonRepository, UserRatingMapper userRatingMapper) {
        this.userRatingRepository = userRatingRepository;
        this.userRepository = userRepository;
        this.webtoonRepository = webtoonRepository;
        this.userRatingMapper = userRatingMapper;  // Mapper 주입
    }

    public UserRatingDTO createUserRating(Long userId, Long webtoonId, float rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new IllegalArgumentException("Webtoon not found"));

        UserRating userRating = UserRating.builder()
                .user(user)
                .webtoon(webtoon)
                .rating(rating)
                .comment(comment)
                .likes(0)
                .modifyDate(LocalDate.now())
                .build();

        UserRating savedUserRating = userRatingRepository.save(userRating);

        return userRatingMapper.toDTO(savedUserRating);
    }

    public UserRatingDTO updateUserRating(Long ratingId, float rating, String comment, int likes) {
        UserRating userRating = userRatingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        userRating.update(rating, comment, LocalDate.now());
        UserRating updatedUserRating = userRatingRepository.save(userRating);

        return userRatingMapper.toDTO(updatedUserRating);
    }

    public Page<UserRatingDTO> getRatingsByLikes(Long webtoonId, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("likes").descending());
        Page<UserRating> userRatings = userRatingRepository.findByWebtoonIdOrderByLikesDesc(webtoonId, pageable);

        // 엔티티 페이지를 DTO 페이지로 변환
        return userRatings.map(userRatingMapper::toDTO);
    }

    public Page<UserRatingDTO> getRatingsByLatest(Long webtoonId, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("modifyDate").descending());
        Page<UserRating> userRatings = userRatingRepository.findByWebtoonIdOrderByModifyDateDesc(webtoonId, pageable);

        return userRatings.map(userRatingMapper::toDTO);
    }

    public List<UserRatingDTO> getRatingsForWebtoonAll(Long webtoonId) {
        List<UserRating> userRatings = userRatingRepository.findByWebtoonId(webtoonId);

        return userRatings.stream()
                .map(userRatingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<UserRatingDTO> getRatingsByUser(Long userId) {
        List<UserRating> userRatings = userRatingRepository.findByUserId(userId);

        return userRatings.stream()
                .map(userRatingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteUserRating(Long ratingId) {
        userRatingRepository.deleteById(ratingId);
    }
}
