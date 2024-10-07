package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toonpick.app.entity.ReviewLike;
import toonpick.app.entity.User;
import toonpick.app.entity.WebtoonReview;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByUserAndReview(User user, WebtoonReview review);
}
