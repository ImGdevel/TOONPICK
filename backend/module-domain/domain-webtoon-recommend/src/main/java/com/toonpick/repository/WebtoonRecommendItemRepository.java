package com.toonpick.repository;

import com.toonpick.entity.WebtoonRecommendItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonRecommendItemRepository extends JpaRepository<WebtoonRecommendItem, Long> {
}
