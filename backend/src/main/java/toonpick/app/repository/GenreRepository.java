package toonpick.app.repository;

import toonpick.app.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByName(String name);
    Optional<Genre> findByName(String name);
}
