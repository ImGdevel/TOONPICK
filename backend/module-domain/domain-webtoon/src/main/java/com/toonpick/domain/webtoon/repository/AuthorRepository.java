package com.toonpick.domain.webtoon.repository;


import com.toonpick.domain.webtoon.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String name);
    boolean existsByUid(String uid);
    Optional<Author> findByName(String name);
    Optional<Author> findByUid(String uid);
}
