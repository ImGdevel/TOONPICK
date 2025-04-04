package com.toonpick.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.toonpick.entity.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
    Optional<Author> findByUid(String uid);
}
