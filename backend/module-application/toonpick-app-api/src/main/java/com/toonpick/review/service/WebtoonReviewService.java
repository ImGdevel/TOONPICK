package com.toonpick.review.service;

import com.toonpick.dto.PagedResponseDTO;
import com.toonpick.entity.Member;
import com.toonpick.entity.Webtoon;
import com.toonpick.exception.DatabaseOperationException;
import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.repository.MemberRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.repository.WebtoonReviewRepository;
import com.toonpick.repository.ReviewLikeRepository;
import com.toonpick.type.ErrorCode;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import com.toonpick.dto.WebtoonReviewCreateDTO;
import com.toonpick.dto.WebtoonReviewDTO;
import com.toonpick.entity.ReviewLike;
import com.toonpick.entity.WebtoonReview;
import com.toonpick.review.mapper.WebtoonReviewMapper;

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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(reviewCreateDTO.getWebtoonId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, reviewCreateDTO.getWebtoonId()));

        WebtoonReview review = WebtoonReview.builder()
                .member(member)
                .webtoon(webtoon)
                .comment(reviewCreateDTO.getComment())
                .rating(reviewCreateDTO.getRating())
                .likes(0)
                .build();

        WebtoonReview savedReview = webtoonReviewRepository.save(review);

        webtoonRepository.addReview(webtoon.getId(), reviewCreateDTO.getRating());

        return webtoonReviewMapper.webtoonReviewToWebtoonReviewDTO(savedReview);
    }

    // 리뷰 가져오기
    @Transactional(readOnly = true)
    public WebtoonReviewDTO getReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REVIEW_NOT_FOUND, reviewId));

        return webtoonReviewMapper.webtoonReviewToWebtoonReviewDTO(review);
    }

    // 리뷰 수정
    @Transactional
    public WebtoonReviewDTO updateReview(Long reviewId, WebtoonReviewCreateDTO reviewCreateDTO) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REVIEW_NOT_FOUND, reviewId));
        float oldRating = review.getRating();
        review.update(reviewCreateDTO.getRating(), reviewCreateDTO.getComment());

        WebtoonReview updatedReview = webtoonReviewRepository.save(review);
        webtoonRepository.updateReview(review.getWebtoon().getId(), oldRating, reviewCreateDTO.getRating());

        return webtoonReviewMapper.webtoonReviewToWebtoonReviewDTO(updatedReview);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REVIEW_NOT_FOUND, reviewId));
        webtoonRepository.removeReview(review.getWebtoon().getId(), review.getRating());

        webtoonReviewRepository.delete(review);
    }

    // 리뷰 좋아요 체크
    @Async
    @Transactional
    public CompletableFuture<Boolean> toggleLike(String username, Long reviewId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REVIEW_NOT_FOUND, reviewId));

        try {
            Optional<ReviewLike> existingLike = reviewLikeRepository.findByMemberAndReview(member, review);

            if (existingLike.isPresent()) {
                // 좋아요 취소
                reviewLikeRepository.delete(existingLike.get());
                updateLikeCountAsync(reviewId, false);
                return CompletableFuture.completedFuture(false);
            } else {
                // 좋아요 추가
                reviewLikeRepository.save(new ReviewLike(member, review));
                updateLikeCountAsync(reviewId, true);
                return CompletableFuture.completedFuture(true);
            }
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(ErrorCode.DATABASE_ERROR, e);
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.UNKNOWN_ERROR.getMessage(), e);
        }
    }

    @Async
    public CompletableFuture<Void> updateLikeCountAsync(Long reviewId, boolean up) {
        try {
            if (up) {
                webtoonReviewRepository.incrementLikes(reviewId);
            } else {
                webtoonReviewRepository.decrementLikes(reviewId);
            }
        } catch (DataAccessException e) {
            throw new DatabaseOperationException(ErrorCode.LIKE_COUNT_UPDATE_ERROR, e);
        }
        return CompletableFuture.completedFuture(null);
    }


    // User가 웹툰에 누른 웹툰 리스트 반환
    @Transactional(readOnly = true)
    public List<Long> getLikedReviewIds(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));

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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));

        Sort sort;
        if ("best".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "likes");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdDate");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WebtoonReview> reviewPage = webtoonReviewRepository.findByWebtoon(webtoon, pageable);

        List<WebtoonReviewDTO> reviewDTOs = reviewPage.getContent().stream()
                .map(webtoonReviewMapper::webtoonReviewToWebtoonReviewDTO)
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, webtoonId));

        Optional<WebtoonReview> reviewOpt = webtoonReviewRepository.findWebtoonReviewByMemberAndWebtoon(member, webtoon);
        return reviewOpt.map(webtoonReviewMapper::webtoonReviewToWebtoonReviewDTO);
    }
}
