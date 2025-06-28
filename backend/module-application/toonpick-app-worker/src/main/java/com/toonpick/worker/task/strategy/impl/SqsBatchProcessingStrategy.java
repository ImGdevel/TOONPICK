package com.toonpick.worker.task.strategy.impl;

import com.toonpick.worker.dto.payload.WebtoonCrawItem;
import com.toonpick.worker.dto.payload.WebtoonEpisodeCrawItem;
import com.toonpick.worker.infrastructure.messaging.publisher.WebtoonUpdatePublisher;
import com.toonpick.worker.task.strategy.BatchProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SQS 배치 처리 전략
 * 웹툰 데이터를 SQS 메시지로 배치 전송하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SqsBatchProcessingStrategy implements BatchProcessingStrategy {
    
    private final WebtoonUpdatePublisher webtoonUpdatePublisher;
    
    private static final int DEFAULT_BATCH_SIZE = 20;
    
    @Override
    public void processBatch(List<?> items) {
        if (items == null || items.isEmpty()) {
            log.warn("처리할 아이템이 없습니다.");
            return;
        }
        
        // 아이템 타입에 따른 처리
        if (items.get(0) instanceof WebtoonCrawItem) {
            processWebtoonCrawItems((List<WebtoonCrawItem>) items);
        } else if (items.get(0) instanceof WebtoonEpisodeCrawItem) {
            processWebtoonEpisodeCrawItems((List<WebtoonEpisodeCrawItem>) items);
        } else {
            log.error("지원하지 않는 아이템 타입: {}", items.get(0).getClass().getSimpleName());
        }
    }
    
    private void processWebtoonCrawItems(List<WebtoonCrawItem> items) {
        log.info("웹툰 크롤링 아이템 배치 처리 시작: {} 개", items.size());
        
        for (int i = 0; i < items.size(); i += getBatchSize()) {
            int end = Math.min(i + getBatchSize(), items.size());
            List<WebtoonCrawItem> batch = items.subList(i, end);
            webtoonUpdatePublisher.sendWebtoonUpdateRequest(batch);
            log.debug("웹툰 업데이트 요청 전송: {} ~ {} / {}", i, end - 1, items.size());
        }
    }
    
    private void processWebtoonEpisodeCrawItems(List<WebtoonEpisodeCrawItem> items) {
        log.info("웹툰 에피소드 크롤링 아이템 배치 처리 시작: {} 개", items.size());
        
        for (int i = 0; i < items.size(); i += getBatchSize()) {
            int end = Math.min(i + getBatchSize(), items.size());
            List<WebtoonEpisodeCrawItem> batch = items.subList(i, end);
            webtoonUpdatePublisher.sendWebtoonEpisodeUpdateRequest(batch);
            log.debug("에피소드 업데이트 요청 전송: {} ~ {} / {}", i, end - 1, items.size());
        }
    }
    
    @Override
    public int getBatchSize() {
        return DEFAULT_BATCH_SIZE;
    }
    
    @Override
    public String getStrategyName() {
        return "SQS_BATCH_PROCESSING";
    }
    
    @Override
    public Class<?> getSupportedDataType() {
        return WebtoonCrawItem.class;
    }
} 