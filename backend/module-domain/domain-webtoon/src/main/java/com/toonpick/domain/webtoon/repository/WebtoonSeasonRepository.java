package com.toonpick.domain.webtoon.repository;

import com.toonpick.domain.webtoon.entity.WebtoonSeason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonSeasonRepository extends JpaRepository<WebtoonSeason, Long> {
}
