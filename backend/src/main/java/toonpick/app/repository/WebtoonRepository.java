package toonpick.app.repository;

import toonpick.app.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    List<Webtoon> findByAuthors_Name(String authorName);
    List<Webtoon> findByGenres_Name(String genreName);

    List<Webtoon> findByWeek(DayOfWeek week);
}
