package toonpick.app.webtoon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.webtoon.entity.Webtoon;
import toonpick.app.webtoon.entity.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long>, WebtoonRepositoryCustom {
   Optional<Webtoon> findByPlatformId(String platformId);

    List<Webtoon> findByAuthors_Name(String authorName);
    List<Webtoon> findByGenres_Name(String genreName);

    Page<Webtoon> findBySerializationStatus(SerializationStatus status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Webtoon w SET w.ratingSum = w.ratingSum + :newRating, " +
           "w.ratingCount = w.ratingCount + 1, " +
           "w.averageRating = w.ratingSum / w.ratingCount " +
           "WHERE w.id = :webtoonId")
    void addReview(@Param("webtoonId") Long webtoonId, @Param("newRating") float newRating);


    @Modifying
    @Transactional
    @Query("UPDATE Webtoon w SET w.ratingSum = w.ratingSum - :oldRating, " +
           "w.ratingCount = w.ratingCount - 1, " +
           "w.averageRating = CASE WHEN w.ratingCount = 0 THEN 0 ELSE w.ratingSum / w.ratingCount END " +
           "WHERE w.id = :webtoonId")
    void removeReview(@Param("webtoonId") Long webtoonId, @Param("oldRating") float oldRating);


    @Modifying
    @Transactional
    @Query("UPDATE Webtoon w SET w.ratingSum = w.ratingSum - :oldRating + :newRating, " +
           "w.averageRating = w.ratingSum / w.ratingCount " +
           "WHERE w.id = :webtoonId")
    void updateReview(@Param("webtoonId") Long webtoonId, @Param("oldRating") float oldRating, @Param("newRating") float newRating);

}
