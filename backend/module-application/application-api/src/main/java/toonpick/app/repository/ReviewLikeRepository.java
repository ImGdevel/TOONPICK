package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toonpick.app.domain.review.ReviewLike;
import toonpick.app.domain.member.Member;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.domain.review.WebtoonReview;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByMemberAndReview(Member member, WebtoonReview review);

    @Query("SELECT rl FROM ReviewLike rl WHERE rl.member = :member AND rl.review.webtoon = :webtoon")
    List<ReviewLike> findByMemberAndWebtoon(@Param("member") Member member, @Param("webtoon") Webtoon webtoon);
}
