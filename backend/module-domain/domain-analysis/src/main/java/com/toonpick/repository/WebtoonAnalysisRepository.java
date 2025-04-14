package com.toonpick.repository;

import com.toonpick.entity.WebtoonAnalysisDataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WebtoonAnalysisRepository extends MongoRepository<WebtoonAnalysisDataDocument, String> {
    Optional<WebtoonAnalysisDataDocument> findByWebtoonId(Long webtoonId);
}
