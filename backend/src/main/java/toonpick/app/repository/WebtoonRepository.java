package toonpick.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import toonpick.app.domain.webtoon.Webtoon;
import toonpick.app.dto.webtoon.WebtoonUpdateRequest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long>, WebtoonRepositoryCustom {
   Optional<Webtoon> findByExternalId(String externalId);

   // 쿼리 설명 : 연재중인 웹툰중 업데이트 요일이거나 휴재중인 웹툰 중 오늘 업데이트 되지 않은 웹툰 반환
    @Query("SELECT new com.example.dto.WebtoonUpdateRequest(w.id, w.title, w.link, w.platform, w.serializationStatus) " +
           "FROM Webtoon w " +
           "WHERE w.lastUpdatedDate < :today " +
           "AND (" +
           "    (w.serializationStatus = 'ONGOING' AND w.dayOfWeek = :tomorrow) " +
           "    OR w.serializationStatus = 'HIATUS'" +
           ")")
    List<WebtoonUpdateRequest> findWebtoonsForUpdate(@Param("today") LocalDate today, @Param("tomorrow") DayOfWeek tomorrow, Pageable pageable);

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
