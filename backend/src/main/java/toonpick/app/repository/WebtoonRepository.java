package toonpick.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import toonpick.app.dto.WebtoonFilterDTO;
import toonpick.app.entity.Webtoon;
import toonpick.app.entity.enums.SerializationStatus;

import java.time.DayOfWeek;
import java.util.List;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long>, WebtoonRepositoryCustom {
    List<Webtoon> findByAuthors_Name(String authorName);
    List<Webtoon> findByGenres_Name(String genreName);

    List<Webtoon> findByWeek(DayOfWeek week);
    List<Webtoon> findBySerializationStatus(SerializationStatus status);

    Page<Webtoon> findBySerializationStatus(SerializationStatus status, Pageable pageable);


}
