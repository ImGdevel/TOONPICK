package toonpick.app.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.member.entity.Member;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.review.entity.WebtoonReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebtoonReviewRepository extends JpaRepository<WebtoonReview, Long> {

    List<WebtoonReview> findByWebtoon(Webtoon webtoon);

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
