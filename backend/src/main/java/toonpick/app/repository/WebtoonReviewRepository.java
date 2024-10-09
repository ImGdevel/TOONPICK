package toonpick.app.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.WebtoonReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebtoonReviewRepository extends JpaRepository<WebtoonReview, Long> {

    List<WebtoonReview> findByWebtoon(Webtoon webtoon);

    Page<WebtoonReview> findByWebtoon(Webtoon webtoon, Pageable pageable);

    Optional<WebtoonReview> findWebtoonReviewByUserAndWebtoon(User user, Webtoon webtoon);

    @Modifying
    @Query("UPDATE WebtoonReview r SET r.likes = r.likes + 1 WHERE r.id = :reviewId")
    void incrementLikes(@Param("reviewId") Long reviewId);

    @Modifying
    @Query("UPDATE WebtoonReview r SET r.likes = r.likes - 1 WHERE r.id = :reviewId AND r.likes > 0")
    void decrementLikes(@Param("reviewId") Long reviewId);

}
