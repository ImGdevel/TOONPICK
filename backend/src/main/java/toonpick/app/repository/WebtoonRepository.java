package toonpick.app.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toonpick.app.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    List<Webtoon> findByAuthors_Name(String authorName);
    List<Webtoon> findByGenres_Name(String genreName);

    List<Webtoon> findByWeek(DayOfWeek week);
    List<Webtoon> findByStatus(String status);

    @Query("SELECT w FROM Webtoon w WHERE w.week = :week AND w.status IN (:statuses)")
    List<Webtoon> findWebtoonsByWeekAndStatusIn(@Param("week") DayOfWeek week, @Param("statuses") List<String> statuses);
}
