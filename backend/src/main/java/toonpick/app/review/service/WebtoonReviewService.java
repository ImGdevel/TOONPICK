package toonpick.app.review.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.dto.PagedResponseDTO;
import toonpick.app.review.dto.WebtoonReviewCreateDTO;
import toonpick.app.review.dto.WebtoonReviewDTO;
import toonpick.app.review.entity.ReviewLike;
import toonpick.app.member.entity.Member;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.review.entity.WebtoonReview;
import toonpick.app.exception.ResourceNotFoundException;
import toonpick.app.review.mapper.WebtoonReviewMapper;
import toonpick.app.review.repository.ReviewLikeRepository;
import toonpick.app.member.repository.MemberRepository;
import toonpick.app.repository.WebtoonRepository;
import toonpick.app.review.repository.WebtoonReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebtoonReviewService {

    private final WebtoonReviewRepository webtoonReviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;

    private final WebtoonReviewMapper webtoonReviewMapper = WebtoonReviewMapper.INSTANCE;

    // 리뷰 추가
    @Transactional
    public WebtoonReviewDTO createReview(WebtoonReviewCreateDTO reviewCreateDTO, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(reviewCreateDTO.getWebtoonId())
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        WebtoonReview review = WebtoonReview.builder()
                .member(member)
                .webtoon(webtoon)
                .comment(reviewCreateDTO.getComment())
                .rating(reviewCreateDTO.getRating())
                .likes(0)
                .build();

        WebtoonReview savedReview = webtoonReviewRepository.save(review);

        webtoonRepository.addReview(webtoon.getId(), reviewCreateDTO.getRating());

        return webtoonReviewMapper.toDTO(savedReview);
    }

    // 리뷰 가져오기
    @Transactional(readOnly = true)
    public WebtoonReviewDTO getReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        return webtoonReviewMapper.toDTO(review);
    }

    // 리뷰 업데이트
    @Transactional
    public WebtoonReviewDTO updateReview(Long reviewId, WebtoonReviewCreateDTO reviewCreateDTO) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        float oldRating = review.getRating();
        review.update(reviewCreateDTO.getRating(), reviewCreateDTO.getComment());

        WebtoonReview updatedReview = webtoonReviewRepository.save(review);
        webtoonRepository.updateReview(review.getWebtoon().getId(), oldRating, reviewCreateDTO.getRating());

        return webtoonReviewMapper.toDTO(updatedReview);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        webtoonRepository.removeReview(review.getWebtoon().getId(), review.getRating());

        webtoonReviewRepository.delete(review);
    }

    // 리뷰 체크
    @Async
    @Transactional
    public CompletableFuture<Boolean> toggleLike(String username, Long reviewId) {
        try {
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
            WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

            Optional<ReviewLike> existingLike = reviewLikeRepository.findByMemberAndReview(member, review);

            boolean liked;

            if (existingLike.isPresent()) {
                // 좋아요 취소
                reviewLikeRepository.delete(existingLike.get());
                liked = false;
                updateLikeCountAsync(reviewId, false);
            } else {
                // 좋아요 추가
                reviewLikeRepository.save(new ReviewLike(member, review));
                liked = true;
                updateLikeCountAsync(reviewId, true);
            }

            return CompletableFuture.completedFuture(liked);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Review or Member not found: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Database integrity issue while processing like", e);
        } catch (Exception e) {
            throw new RuntimeException("Error: ", e);
        }
    }

    @Async
    public CompletableFuture<Void> updateLikeCountAsync(Long reviewId, boolean up) {
        try {
            if(up){
                webtoonReviewRepository.incrementLikes(reviewId);
            }else{
                webtoonReviewRepository.decrementLikes(reviewId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating like count asynchronously", e);
        }

        return CompletableFuture.completedFuture(null);
    }

    // User가 웹툰에 누른 웹툰 리스트 반환
    @Transactional(readOnly = true)
    public List<Long> getLikedReviewIds(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        List<ReviewLike> likes = reviewLikeRepository.findByMemberAndWebtoon(member, webtoon);
        return likes.stream()
                .map(like -> like.getReview().getId())
                .collect(Collectors.toList());
    }

    // 특정 웹툰의 리뷰들을 가져온다 (정렬 기준)
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
                .data(reviewDTOs)
                .page(reviewPage.getNumber())
                .size(reviewPage.getSize())
                .totalElements(reviewPage.getTotalElements())
                .totalPages(reviewPage.getTotalPages())
                .last(reviewPage.isLast())
                .build();
    }

    // 어떤 유저가 특정 웹툰에 작성한 리뷰를 가져온다
    @Transactional(readOnly = true)
    public Optional<WebtoonReviewDTO> getUserReviewForWebtoon(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException("Webtoon not found"));

        Optional<WebtoonReview> reviewOpt = webtoonReviewRepository.findWebtoonReviewByMemberAndWebtoon(member, webtoon);
        return reviewOpt.map(webtoonReviewMapper::toDTO);
    }
}
