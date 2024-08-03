package toonpick.app.repository;

import toonpick.app.entity.InterestGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestGenreRepository extends JpaRepository<InterestGenre, Long> {
    // 추가적인 쿼리 메서드 정의 가능
}