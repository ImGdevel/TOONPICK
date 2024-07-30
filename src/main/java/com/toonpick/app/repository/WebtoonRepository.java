package com.toonpick.app.repository;

import com.toonpick.app.entity.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    List<Webtoon> findByGenre(String genre);
}
