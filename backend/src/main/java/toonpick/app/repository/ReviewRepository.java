package toonpick.app.repository;

import toonpick.app.entity.Review;
import toonpick.app.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByWebtoon(Webtoon webtoon);
}