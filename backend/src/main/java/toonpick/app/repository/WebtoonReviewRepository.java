package toonpick.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toonpick.app.entity.User;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.WebtoonReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebtoonReviewRepository extends JpaRepository<WebtoonReview, Long> {
    List<WebtoonReview> findByWebtoon(Webtoon webtoon);

    Page<WebtoonReview> findByWebtoon(Webtoon webtoon, Pageable pageable);

    Optional<WebtoonReview> findByUserAndWebtoon(User user, Webtoon webtoon);
}
