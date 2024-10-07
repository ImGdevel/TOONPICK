package toonpick.app.service;

import jakarta.persistence.OptimisticLockException;
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


    public WebtoonReviewDTO createReview(WebtoonReviewCreateDTO reviewCreateDTO) {
        User user = userRepository.findById(reviewCreateDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Webtoon webtoon = webtoonRepository.findById(reviewCreateDTO.getWebtoonId())
                .orElseThrow(() -> new ResourceNotFoundException("웹툰을 찾을 수 없습니다."));

        WebtoonReview review = WebtoonReview.builder()
                .user(user)
                .webtoon(webtoon)
                .comment(reviewCreateDTO.getComment())
                .rating(reviewCreateDTO.getRating())
                .likes(0)
                .build();

        WebtoonReview savedReview = webtoonReviewRepository.save(review);

        return webtoonReviewMapper.toDTO(savedReview);
    }

    @Transactional(readOnly = true)
    public WebtoonReviewDTO getReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다."));

        return webtoonReviewMapper.toDTO(review);
    }

    public WebtoonReviewDTO updateReview(Long reviewId, WebtoonReviewCreateDTO reviewCreateDTO) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다."));
        review.update(reviewCreateDTO.getRating(), reviewCreateDTO.getComment());

        WebtoonReview updatedReview = webtoonReviewRepository.save(review);

        return webtoonReviewMapper.toDTO(updatedReview);
    }


    public void deleteReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다."));
        webtoonReviewRepository.delete(review);
    }


    public WebtoonReviewDTO toggleLike(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다."));

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByUserAndReview(user, review);

        if (existingLike.isPresent()) {
            reviewLikeRepository.delete(existingLike.get());
            review.decreaseLikes();
        } else {
            reviewLikeRepository.save(new ReviewLike(user, review));
            review.increaseLikes();
        }

        // 동시성 문제 방지를 위해 Optimistic Locking 적용
        try {
            WebtoonReview updatedReview = webtoonReviewRepository.save(review);
            return webtoonReviewMapper.toDTO(updatedReview);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("좋아요 처리 중 동시성 문제가 발생했습니다.");
        }
    }


    @Transactional(readOnly = true)
    public PagedResponseDTO<WebtoonReviewDTO> getReviewsByWebtoon(
            Long webtoonId, String sortBy, int page, int size) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("웹툰을 찾을 수 없습니다."));

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
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("웹툰을 찾을 수 없습니다."));

        Optional<WebtoonReview> reviewOpt = webtoonReviewRepository.findByUserAndWebtoon(user, webtoon);
        return reviewOpt.map(webtoonReviewMapper::toDTO);
    }

}
