package com.toonpick.domain.webtoon.repository;

import com.toonpick.domain.webtoon.entity.SimilarWebtoon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimilarWebtoonRepository extends JpaRepository<SimilarWebtoon, Long> {
}
