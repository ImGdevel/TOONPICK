package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toonpick.app.entity.UserFavoriteWebtoon;

import java.util.Optional;

@Repository
public interface UserFavoriteWebtoonRepository extends JpaRepository<UserFavoriteWebtoon, Long> {
    Optional<UserFavoriteWebtoon> findByUserIdAndWebtoonId(Long userId, Long webtoonId);
}
