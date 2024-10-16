package toonpick.app.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.PagedResponseDTO;
import toonpick.app.dto.WebtoonReviewCreateDTO;
import toonpick.app.dto.WebtoonReviewDTO;
import toonpick.app.entity.ReviewLike;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.WebtoonReview;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.mapper.WebtoonReviewMapper;
import toonpick.app.repository.ReviewLikeRepository;
import toonpick.app.repository.UserRepository;
import toonpick.app.repository.WebtoonRepository;
import toonpick.app.repository.WebtoonReviewRepository;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WebtoonReviewService {

    private final WebtoonReviewRepository webtoonReviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final WebtoonRepository webtoonRepository;

    private final WebtoonReviewMapper webtoonReviewMapper = WebtoonReviewMapper.INSTANCE;

    public WebtoonReviewDTO createReview(WebtoonReviewCreateDTO reviewCreateDTO, Long webtoonId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        WebtoonReview review = WebtoonReview.builder()
                .user(user)
                .webtoon(webtoon)
                .comment(reviewCreateDTO.getComment())
                .rating(reviewCreateDTO.getRating())
                .likes(0)
                .build();

        WebtoonReview savedReview = webtoonReviewRepository.save(review);

        webtoonRepository.addReview(webtoon.getId(), reviewCreateDTO.getRating());



        return webtoonReviewMapper.toDTO(savedReview);
    }

    @Transactional(readOnly = true)
    public WebtoonReviewDTO getReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        return webtoonReviewMapper.toDTO(review);
    }

    public WebtoonReviewDTO updateReview(Long reviewId, WebtoonReviewCreateDTO reviewCreateDTO) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        float oldRating = review.getRating();
        review.update(reviewCreateDTO.getRating(), reviewCreateDTO.getComment());

        WebtoonReview updatedReview = webtoonReviewRepository.save(review);
        webtoonRepository.updateReview(review.getWebtoon().getId(), oldRating, reviewCreateDTO.getRating());

        return webtoonReviewMapper.toDTO(updatedReview);
    }

    public void deleteReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        webtoonRepository.removeReview(review.getWebtoon().getId(), review.getRating());

        webtoonReviewRepository.delete(review);
    }

    @Transactional
    public synchronized Boolean toggleLike(Long userId, Long reviewId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

            Optional<ReviewLike> existingLike = reviewLikeRepository.findByUserAndReview(user, review);

            boolean liked;

            if (existingLike.isPresent()) {
                reviewLikeRepository.delete(existingLike.get());
                webtoonReviewRepository.decrementLikes(reviewId);
                liked = false;
            } else {
                reviewLikeRepository.save(new ReviewLike(user, review));
                webtoonReviewRepository.incrementLikes(reviewId);
                liked = true;
            }

            return liked;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("An issue occurred while processing the like", e);
        }
    }


    @Transactional(readOnly = true)
    public List<Long> getLikedReviewIds(Long userId, Long webtoonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        List<ReviewLike> likes = reviewLikeRepository.findByUserAndWebtoon(user, webtoon);
        return likes.stream()
                .map(like -> like.getReview().getId())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<WebtoonReviewDTO> getReviewsByWebtoon(
            Long webtoonId, String sortBy, int page, int size) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        Sort sort;
        if ("best".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "likes");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdDate");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WebtoonReview> reviewPage = webtoonReviewRepository.findByWebtoon(webtoon, pageable);

        List<WebtoonReviewDTO> reviewDTOs = reviewPage.getContent().stream()
                .map(webtoonReviewMapper::toDTO)
                .collect(Collectors.toList());

        return PagedResponseDTO.<WebtoonReviewDTO>builder()
                .content(reviewDTOs)
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .last(reviewPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<WebtoonReviewDTO> getUserReviewForWebtoon(Long userId, Long webtoonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        Optional<WebtoonReview> reviewOpt = webtoonReviewRepository.findWebtoonReviewByUserAndWebtoon(user, webtoon);
        return reviewOpt.map(webtoonReviewMapper::toDTO);
    }
}
