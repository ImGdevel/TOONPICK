package com.toonpick.review.service;

import com.toonpick.dto.PagedResponseDTO;
import com.toonpick.entity.Member;
import com.toonpick.entity.ReviewLike;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonReview;
import com.toonpick.exception.EntityNotFoundException;
import com.toonpick.exception.InternalServerException;
import com.toonpick.repository.MemberRepository;
import com.toonpick.repository.ReviewLikeRepository;
import com.toonpick.repository.WebtoonRepository;
import com.toonpick.repository.WebtoonReviewRepository;
import com.toonpick.repository.WebtoonStatisticsRepository;
import com.toonpick.review.mapper.WebtoonReviewMapper;
import com.toonpick.review.request.WebtoonReviewCreateRequest;
import com.toonpick.review.request.WebtoonReviewUpdateRequest;
import com.toonpick.review.response.WebtoonReviewResponse;
import com.toonpick.type.ErrorCode;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebtoonReviewService {

    private final WebtoonReviewRepository webtoonReviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MemberRepository memberRepository;
    private final WebtoonRepository webtoonRepository;
    private final WebtoonStatisticsRepository webtoonStatisticsRepository;

    private final WebtoonReviewMapper webtoonReviewMapper = WebtoonReviewMapper.INSTANCE;

    // 리뷰 추가
    @Transactional
    public WebtoonReviewResponse createReview(WebtoonReviewCreateRequest reviewCreateRequest, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(reviewCreateRequest.getWebtoonId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, String.valueOf(reviewCreateRequest.getWebtoonId())));

        WebtoonReview review = WebtoonReview.builder()
                .member(member)
                .webtoon(webtoon)
                .comment(reviewCreateRequest.getComment())
                .rating(reviewCreateRequest.getRating())
                .likesCount(0)
                .build();

        WebtoonReview savedReview = webtoonReviewRepository.save(review);

        // todo : Webtoon 평점에 반영(업데이트/비동기 로직 고려)
        webtoonStatisticsRepository.addReview(webtoon.getId(), reviewCreateRequest.getRating());

        return webtoonReviewMapper.toWebtoonReviewResponse(savedReview);
    }

    // 리뷰 가져오기
    @Transactional(readOnly = true)
    public WebtoonReviewResponse getReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND, String.valueOf(reviewId)));

        return webtoonReviewMapper.toWebtoonReviewResponse(review);
    }

    // 리뷰 수정
    @Transactional
    public WebtoonReviewResponse updateReview(Long reviewId, WebtoonReviewUpdateRequest reviewUpdateRequest) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND, String.valueOf(reviewId)));
        float oldRating = review.getRating();

        review.updateRating(reviewUpdateRequest.getRating());
        review.updateComment(reviewUpdateRequest.getComment());

        WebtoonReview updatedReview = webtoonReviewRepository.save(review);

        // todo : Webtoon 평점 업데이트
        webtoonStatisticsRepository.updateReview(review.getWebtoon().getId(), oldRating, reviewUpdateRequest.getRating());

        return webtoonReviewMapper.toWebtoonReviewResponse(updatedReview);
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND, String.valueOf(reviewId)));
        webtoonStatisticsRepository.removeReview(review.getWebtoon().getId(), review.getRating());

        webtoonReviewRepository.delete(review);
    }

    // 리뷰 좋아요 체크
    @Async
    @Transactional
    public CompletableFuture<Boolean> toggleLike(String username, Long reviewId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        WebtoonReview review = webtoonReviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.REVIEW_NOT_FOUND, String.valueOf(reviewId)));

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
            throw new InternalServerException(ErrorCode.DATABASE_ERROR, e);
        } catch (Exception e) {
            throw new InternalServerException(ErrorCode.UNKNOWN_ERROR.getMessage(), e);
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
            throw new InternalServerException(ErrorCode.LIKE_COUNT_UPDATE_ERROR, e);
        }
        return CompletableFuture.completedFuture(null);
    }


    // 특정 웹툰의 리뷰들을 가져온다 (정렬 기준)
    @Transactional(readOnly = true)
    public PagedResponseDTO<WebtoonReviewResponse> getReviewsByWebtoon(
            Long webtoonId, String sortBy, int page, int size, @Nullable String username) {

        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, String.valueOf(webtoonId)));

        Member member = null;
        if (username != null) {
            member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        }

        Sort sort = "best".equalsIgnoreCase(sortBy)
                ? Sort.by(Sort.Direction.DESC, "likes")
                : Sort.by(Sort.Direction.DESC, "createdDate");

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WebtoonReview> reviewPage = webtoonReviewRepository.findByWebtoon(webtoon, pageable);

        List<Long> reviewIds = reviewPage.getContent().stream()
                .map(WebtoonReview::getId)
                .toList();

        Set<Long> likedReviewIds = member != null
                ? new HashSet<>(reviewLikeRepository.findLikedReviewIdsByMemberIdAndReviewIds(member.getId(), reviewIds))
                : Collections.emptySet();

        List<WebtoonReviewResponse> reviewDTOs = reviewPage.getContent().stream()
                .map(review -> {
                    WebtoonReviewResponse dto = webtoonReviewMapper.toWebtoonReviewResponse(review);
                    dto.setIsLiked(likedReviewIds.contains(review.getId()));
                    return dto;
                })
                .collect(Collectors.toList());

        return PagedResponseDTO.<WebtoonReviewResponse>builder()
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
    public Optional<WebtoonReviewResponse> getUserReviewForWebtoon(String username, Long webtoonId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND, username));
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.WEBTOON_NOT_FOUND, String.valueOf(webtoonId)));

        Optional<WebtoonReview> reviewOpt = webtoonReviewRepository.findWebtoonReviewByMemberAndWebtoon(member, webtoon);
        return reviewOpt.map(webtoonReviewMapper::toWebtoonReviewResponse);
    }
}
