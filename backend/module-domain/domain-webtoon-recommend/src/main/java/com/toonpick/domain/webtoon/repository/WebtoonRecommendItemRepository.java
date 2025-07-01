package com.toonpick.domain.webtoon.repository;

import com.toonpick.domain.webtoon.entity.WebtoonRecommendItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonRecommendItemRepository extends JpaRepository<WebtoonRecommendItem, Long> {
}
