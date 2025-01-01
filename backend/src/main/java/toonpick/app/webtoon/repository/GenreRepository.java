package toonpick.app.webtoon.repository;

import toonpick.app.webtoon.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByName(String name);
    Optional<Genre> findByName(String name);
    Set<Genre> findByNameIn(Set<String> names);
}
