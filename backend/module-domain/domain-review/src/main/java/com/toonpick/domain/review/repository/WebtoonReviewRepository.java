package com.toonpick.domain.review.repository;

import com.toonpick.domain.member.entity.Member;
import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.review.entity.WebtoonReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface WebtoonReviewRepository extends JpaRepository<WebtoonReview, Long> {

    Page<WebtoonReview> findByWebtoon(Webtoon webtoon, Pageable pageable);

    Optional<WebtoonReview> findWebtoonReviewByMemberAndWebtoon(Member member, Webtoon webtoon);

    @Modifying
    @Transactional
    @Query("UPDATE WebtoonReview r SET r.likesCount = r.likesCount - 1 WHERE r.id = :reviewId AND r.likesCount > 0")
    int decrementLikes(@Param("reviewId") Long reviewId);

    @Modifying
    @Transactional
    @Query("UPDATE WebtoonReview r SET r.likesCount = r.likesCount + 1 WHERE r.id = :reviewId")
    int incrementLikes(@Param("reviewId") Long reviewId);

}
