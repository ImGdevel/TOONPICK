package com.toonpick.repository;

import com.toonpick.entity.Member;
import com.toonpick.entity.Webtoon;
import com.toonpick.entity.WebtoonReview;
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

    @Transactional
    @Modifying
    @Query("UPDATE WebtoonReview r SET r.likes = r.likes + 1 WHERE r.id = :reviewId")
    int incrementLikes(@Param("reviewId") Long reviewId);

    @Transactional
    @Modifying
    @Query("UPDATE WebtoonReview r SET r.likes = r.likes - 1 WHERE r.id = :reviewId AND r.likes > 0")
    int decrementLikes(@Param("reviewId") Long reviewId);

}
