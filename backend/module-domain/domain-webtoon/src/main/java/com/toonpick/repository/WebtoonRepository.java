package com.toonpick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.toonpick.entity.Webtoon;
import com.toonpick.enums.SerializationStatus;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long>, WebtoonRepositoryCustom {

    boolean existsByExternalId(String externalId);
    Optional<Webtoon> findByExternalId(String externalId);

     /*
     업데이트가 필요한 웹툰 리스트 참조
      */
     @Query("SELECT w FROM Webtoon w " +
            "WHERE w.serializationStatus IN (:statuses) " +
            "AND w.lastUpdatedDate <= :thresholdDate")
     List<Webtoon> findWebtoonsForUpdate(
         @Param("statuses") List<SerializationStatus> statuses,
         @Param("thresholdDate") LocalDate thresholdDate
     );


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
