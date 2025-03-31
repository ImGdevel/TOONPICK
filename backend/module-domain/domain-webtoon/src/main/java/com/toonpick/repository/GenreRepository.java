package com.toonpick.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.toonpick.entity.Genre;

import java.util.Optional;


public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
