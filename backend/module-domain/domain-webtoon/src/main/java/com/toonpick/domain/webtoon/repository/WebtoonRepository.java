package com.toonpick.domain.webtoon.repository;

import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long>, WebtoonRepositoryCustom {

    /**
     * 같은 제목의 웹툰 전부 반환
     */
    List<Webtoon> findAllByTitle(String title);

    /**
     * 제목과 작가명으로 웹툰을 찾습니다.
     * 성능 최적화를 위해 DB 레벨에서 필터링
     */
    @Query("SELECT DISTINCT w FROM Webtoon w " +
           "JOIN FETCH w.authors a " +
           "WHERE w.title = :title " +
           "AND a.name IN :authorNames " +
           "AND SIZE(w.authors) = :authorCount")
    List<Webtoon> findByTitleAndAuthors(@Param("title") String title, 
                                       @Param("authorNames") List<String> authorNames,
                                       @Param("authorCount") int authorCount);

    /**
     * 제목과 작가명으로 정확히 일치하는 웹툰을 찾습니다.
     */
    @Query("SELECT w FROM Webtoon w " +
           "JOIN FETCH w.authors a " +
           "JOIN FETCH w.platforms p " +
           "WHERE w.title = :title " +
           "AND a.name IN :authorNames " +
           "AND SIZE(w.authors) = :authorCount")
    Optional<Webtoon> findExactMatchByTitleAndAuthors(@Param("title") String title, 
                                                     @Param("authorNames") List<String> authorNames,
                                                     @Param("authorCount") int authorCount);

     /**
     업데이트가 필요한 웹툰 리스트 반환
      */
     @Query("SELECT w FROM Webtoon w " +
            "JOIN FETCH w.statistics s " +
            "WHERE w.serializationStatus IN (:statuses) " +
            "AND w.lastUpdatedDate <= :thresholdDate")
     List<Webtoon> findWebtoonsForUpdate(
         @Param("statuses") List<SerializationStatus> statuses,
         @Param("thresholdDate") LocalDate thresholdDate
     );

    @Query("SELECT w FROM Webtoon w " +
           "JOIN FETCH w.statistics s " +
           "WHERE w.serializationStatus IN (:statuses) " +
           "AND (w.dayOfWeek = :today OR w.dayOfWeek = :nextDay) " +
           "AND w.lastUpdatedDate < :thresholdDate")
    List<Webtoon> findWebtoonsForEpisodeUpdate(
            @Param("statuses") List<SerializationStatus> statuses,
            @Param("today") DayOfWeek today,
            @Param("nextDay") DayOfWeek nextDay,
            @Param("thresholdDate") LocalDate thresholdDate
    );
}
