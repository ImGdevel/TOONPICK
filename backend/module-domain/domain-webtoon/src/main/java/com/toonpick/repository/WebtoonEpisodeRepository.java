package com.toonpick.repository;

import com.toonpick.entity.WebtoonEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebtoonEpisodeRepository extends JpaRepository<WebtoonEpisode, Long> {

    /**
     * 웹툰 별 에피소드 조회
     */
    List<WebtoonEpisode> findByWebtoonId(Long webtoonId);

    /**
     * 시즌 별 에피소드 조회
     */
    List<WebtoonEpisode> findByWebtoonSeasonId(Long seasonId);
}
