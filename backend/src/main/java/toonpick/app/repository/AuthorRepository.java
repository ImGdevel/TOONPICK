package toonpick.app.repository;

import toonpick.app.domain.webtoon.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String name);
    Optional<Author> findByName(String name);
    Set<Author> findByNameIn(Set<String> names);
}
