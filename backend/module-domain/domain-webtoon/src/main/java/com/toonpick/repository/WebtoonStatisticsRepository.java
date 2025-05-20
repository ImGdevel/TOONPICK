package com.toonpick.repository;

import com.toonpick.entity.WebtoonStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface WebtoonStatisticsRepository extends JpaRepository<WebtoonStatistics, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE WebtoonStatistics ws SET " +
           "ws.totalRatingSum = ws.totalRatingSum + :newRating, " +
           "ws.totalRatingCount = ws.totalRatingCount + 1, " +
           "ws.averageRating = ws.totalRatingSum / ws.totalRatingCount " +
           "WHERE ws.id = :webtoonId")
    void addReview(@Param("webtoonId") Long webtoonId, @Param("newRating") float newRating);


    @Modifying
    @Transactional
    @Query("UPDATE WebtoonStatistics ws SET " +
           "ws.totalRatingSum = ws.totalRatingSum - :oldRating, " +
           "ws.totalRatingCount = ws.totalRatingCount - 1, " +
           "ws.averageRating = CASE WHEN ws.totalRatingCount = 0 THEN 0 ELSE ws.totalRatingSum / ws.totalRatingCount END " +
           "WHERE ws.id = :webtoonId")
    void removeReview(@Param("webtoonId") Long webtoonId, @Param("oldRating") float oldRating);


    @Modifying
    @Transactional
    @Query("UPDATE WebtoonStatistics ws SET " +
           "ws.totalRatingSum = ws.totalRatingSum - :oldRating + :newRating, " +
           "ws.averageRating = ws.totalRatingSum / ws.totalRatingCount " +
           "WHERE ws.id = :webtoonId")
    void updateReview(@Param("webtoonId") Long webtoonId, @Param("oldRating") float oldRating, @Param("newRating") float newRating);

}
