package toonpick.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import toonpick.app.entity.WebtoonReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebtoonReviewRepository extends JpaRepository<WebtoonReview, Long> {

    List<WebtoonReview> findByUserId(Long userId);

    List<WebtoonReview> findByWebtoonId(Long webtoonId);

    Page<WebtoonReview> findByWebtoonIdOrderByLikesDesc(Long webtoonId, Pageable pageable);

    Page<WebtoonReview> findByWebtoonIdOrderByModifyDateDesc(Long webtoonId, Pageable pageable);
}
