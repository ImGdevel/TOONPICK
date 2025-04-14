package com.toonpick.repository;

import com.toonpick.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.toonpick.entity.ReviewLike;
import com.toonpick.entity.WebtoonReview;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByMemberAndReview(Member member, WebtoonReview review);

    @Query("SELECT rl.review.id FROM ReviewLike rl WHERE rl.member.id = :memberId AND rl.review.id IN :reviewIds")
    List<Long> findLikedReviewIdsByMemberIdAndReviewIds(@Param("memberId") Long memberId, @Param("reviewIds") List<Long> reviewIds);
}
