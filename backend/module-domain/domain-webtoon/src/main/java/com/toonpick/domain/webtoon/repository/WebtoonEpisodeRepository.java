package com.toonpick.domain.webtoon.repository;

import com.toonpick.domain.webtoon.entity.WebtoonEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebtoonEpisodeRepository extends JpaRepository<WebtoonEpisode, Long> {

    /**
     * 웹툰 별 에피소드 조회 + 에피소드 번호 정렬
     */
    List<WebtoonEpisode> findByWebtoonIdOrderByEpisodeNumberAsc(Long webtoonId);

    /**
     * 시즌 별 에피소드 조회 + 에피소드 번호 정렬
     */
    List<WebtoonEpisode> findByWebtoonSeasonIdOrderByEpisodeNumberAsc(Long seasonId);
}
