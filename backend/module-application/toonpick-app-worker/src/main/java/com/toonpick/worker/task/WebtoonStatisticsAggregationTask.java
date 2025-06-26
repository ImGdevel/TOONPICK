package com.toonpick.worker.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 조회수, 리뷰 수 등 통계 재계산 및 캐싱
 */
@Service
@RequiredArgsConstructor
public class WebtoonStatisticsAggregationTask {

    /**
     * 웹툰 통계 정보 재집계
     */
    public void aggregateAndCacheStatistics(){

    }
}
