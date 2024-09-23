package toonpick.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toonpick.app.entity.User;
import toonpick.app.entity.UserFavoriteWebtoon;
import toonpick.app.entity.Webtoon;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFavoriteWebtoonRepository extends JpaRepository<UserFavoriteWebtoon, Long> {
    Optional<UserFavoriteWebtoon> findByUserIdAndWebtoonId(Long userId, Long webtoonId);

    List<Webtoon> findByUser(User user);
}
