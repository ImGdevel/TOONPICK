package com.toonpick.domain.webtoon.repository;

import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.entity.WebtoonPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonPlatformRepository extends JpaRepository<WebtoonPlatform, Long> {
    int countByWebtoon(Webtoon webtoon);
}
