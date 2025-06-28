package com.toonpick.worker.task.strategy;

import com.toonpick.domain.webtoon.entity.Webtoon;
import com.toonpick.domain.webtoon.enums.SerializationStatus;

/**
 * 상태 업데이트 전략 인터페이스
 * 다양한 웹툰 상태 업데이트 방식을 캡슐화하여 런타임에 교체 가능하게 함
 */
public interface StatusUpdateStrategy {
    
    /**
     * 웹툰의 상태를 업데이트합니다.
     * 
     * @param webtoon 업데이트할 웹툰
     */
    void updateStatus(Webtoon webtoon);
    
    /**
     * 이 전략이 설정하는 목표 상태를 반환합니다.
     * 
     * @return 목표 상태
     */
    SerializationStatus getTargetStatus();
    
    /**
     * 전략의 이름을 반환합니다.
     * 
     * @return 전략 이름
     */
    String getStrategyName();
    
    /**
     * 이 전략이 적용 가능한 웹툰인지 확인합니다.
     * 
     * @param webtoon 확인할 웹툰
     * @return 적용 가능 여부
     */
    boolean isApplicable(Webtoon webtoon);
} 