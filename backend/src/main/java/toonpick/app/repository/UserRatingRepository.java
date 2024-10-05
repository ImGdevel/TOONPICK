package toonpick.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toonpick.app.entity.UserRating;
import toonpick.app.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    List<UserRating> findByWebtoon(Webtoon webtoon);
    Optional<UserRating> findByUserIdAndWebtoonId(Long userId, Long webtoonId);

    List<UserRating> findByUserId(Long userId);

    List<UserRating> findByWebtoonId(Long webtoonId);

    Page<UserRating> findByWebtoonIdOrderByLikesDesc(Long webtoonId, Pageable pageable);

    Page<UserRating> findByWebtoonIdOrderByModifyDateDesc(Long webtoonId, Pageable pageable);
}
