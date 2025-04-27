package com.toonpick.service;

import com.toonpick.dto.response.WebtoonReviewResponse;
import com.toonpick.entity.WebtoonReview;
import com.toonpick.exception.ResourceNotFoundException;
import com.toonpick.mapper.WebtoonReviewMapper;
import com.toonpick.repository.WebtoonReviewRepository;
import com.toonpick.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebtoonReviewService {

    private final WebtoonReviewRepository reviewRepository;
    private final WebtoonReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public WebtoonReviewResponse getReview(Long id) {
        WebtoonReview review = reviewRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        return reviewMapper.toWebtoonReviewResponse(review);
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorCode.REVIEW_NOT_FOUND);
        }
        reviewRepository.deleteById(id);
    }
}
