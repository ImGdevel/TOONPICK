package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toonpick.app.entity.ReviewLike;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.WebtoonReview;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByUserAndReview(User user, WebtoonReview review);

    @Query("SELECT rl FROM ReviewLike rl WHERE rl.user = :user AND rl.review.webtoon = :webtoon")
    List<ReviewLike> findByUserAndWebtoon(@Param("user") User user, @Param("webtoon") Webtoon webtoon);
}
