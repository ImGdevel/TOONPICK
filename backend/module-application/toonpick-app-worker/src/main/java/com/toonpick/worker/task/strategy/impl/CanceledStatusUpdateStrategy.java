package com.toonpick.worker.task.strategy.impl;

import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.worker.task.strategy.StatusUpdateStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 연재중단 상태 업데이트 전략
 * 6개월 이상 휴재 상태인 웹툰을 연재중단 상태로 변경하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CanceledStatusUpdateStrategy implements StatusUpdateStrategy {
    
    @Override
    public void updateStatus(Webtoon webtoon) {
        if (!isApplicable(webtoon)) {
            log.debug("웹툰 {}은 연재중단 상태 업데이트 대상이 아닙니다.", webtoon.getId());
            return;
        }
        
        log.info("웹툰 {} 상태를 연재중단으로 변경합니다.", webtoon.getId());
        
        // 실제 구현에서는 웹툰 상태를 연재중단으로 변경하고 저장
        // webtoon.updateStatus(SerializationStatus.CANCELED);
        // webtoonRepository.save(webtoon);
        
        log.info("웹툰 {} 상태 변경 완료: 연재중단", webtoon.getId());
    }
    
    @Override
    public SerializationStatus getTargetStatus() {
        return SerializationStatus.CANCELED;
    }
    
    @Override
    public String getStrategyName() {
        return "CANCELED_STATUS_UPDATE";
    }
    
    @Override
    public boolean isApplicable(Webtoon webtoon) {
        // 연재중단 기준: 6개월 이상 휴재 상태
        if (webtoon.getSerializationStatus() != SerializationStatus.HIATUS) {
            return false;
        }
        
        // 실제 구현에서는 마지막 업데이트일과 현재 날짜를 비교
        // LocalDateTime lastUpdateDate = webtoon.getLastUpdateDate();
        // return lastUpdateDate != null && 
        //        lastUpdateDate.plusMonths(6).isBefore(LocalDateTime.now());
        
        return false; // 임시로 false 반환
    }
} 