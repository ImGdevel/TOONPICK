package com.toonpick.worker.task.strategy.impl;

import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.SerializationStatus;
import com.toonpick.worker.task.strategy.StatusUpdateStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 완결 상태 업데이트 전략
 * 에피소드 종료 후 7일이 지난 웹툰을 완결 상태로 변경하는 전략
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CompletedStatusUpdateStrategy implements StatusUpdateStrategy {
    
    @Override
    public void updateStatus(Webtoon webtoon) {
        if (!isApplicable(webtoon)) {
            log.debug("웹툰 {}은 완결 상태 업데이트 대상이 아닙니다.", webtoon.getId());
            return;
        }
        
        log.info("웹툰 {} 상태를 완결로 변경합니다.", webtoon.getId());
        
        // 실제 구현에서는 웹툰 상태를 완결로 변경하고 저장
        // webtoon.updateStatus(SerializationStatus.COMPLETED);
        // webtoonRepository.save(webtoon);
        
        log.info("웹툰 {} 상태 변경 완료: 완결", webtoon.getId());
    }
    
    @Override
    public SerializationStatus getTargetStatus() {
        return SerializationStatus.COMPLETED;
    }
    
    @Override
    public String getStrategyName() {
        return "COMPLETED_STATUS_UPDATE";
    }
    
    @Override
    public boolean isApplicable(Webtoon webtoon) {
        // 완결 기준: 에피소드 종료 후 7일이 지난 웹툰
        if (webtoon.getSerializationStatus() != SerializationStatus.ONGOING) {
            return false;
        }
        
        // todo : 에피소드 종료일과 현재 날짜를 비교
        // LocalDateTime lastEpisodeDate = webtoon.getLastEpisodeDate();
        // return lastEpisodeDate != null && 
        //        lastEpisodeDate.plusDays(7).isBefore(LocalDateTime.now());
        
        return false; // 임시로 false 반환
    }
} 