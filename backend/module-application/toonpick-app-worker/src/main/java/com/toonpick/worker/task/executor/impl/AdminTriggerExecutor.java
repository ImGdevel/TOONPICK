package com.toonpick.worker.task.executor;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.strategy.TaskContext;
import com.toonpick.worker.task.strategy.TaskExecutionStrategy;
import com.toonpick.worker.task.strategy.StrategyFactory;
import com.toonpick.common.exception.BadRequestException;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import com.toonpick.worker.dto.payload.WebtoonCrawItem;
import com.toonpick.worker.infrastructure.messaging.publisher.WebtoonUpdatePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 관리자 트리거 비즈니스 실행자
 * 관리자가 요청한 웹툰 트리거 작업을 실행하는 클래스 (실제 비즈니스 로직 포함)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminTriggerExecutor implements TaskExecutor {
    
    private final StrategyFactory strategyFactory;
    private final WebtoonUpdatePublisher webtoonUpdatePublisher;
    
    @Override
    public void execute(TaskContext context) {
        if (!validate(context)) {
            log.error("관리자 트리거 작업 검증 실패: {}", context);
            throw new IllegalArgumentException("Invalid task context");
        }
        
        log.info("관리자 트리거 작업 시작: {}", context.getTaskId());
        
        try {
            // 관리자 트리거 실행 전략 가져오기
            TaskExecutionStrategy strategy = strategyFactory.getTaskExecutionStrategy(TaskType.WEBTOON_ADMIN_TRIGGER);
            
            // 전략 실행
            strategy.execute(context);
            
            // 기존 로직: 웹툰 트리거 요청 처리
            // context에서 WebtoonTriggerRequest 리스트를 안전하게 가져와서 처리
            List<WebtoonTriggerRequest> requests = context.getDataAsList("requests");
            if (requests != null) {
                processWebtoonTriggerRequests(requests);
            } else {
                log.warn("requests 데이터가 없거나 올바른 타입이 아닙니다");
            }
            
            log.info("관리자 트리거 작업 완료: {}", context.getTaskId());
            
        } catch (Exception e) {
            log.error("관리자 트리거 작업 실패: {}", context.getTaskId(), e);
            throw e;
        }
    }
    
    /**
     * 웹툰 등록 처리
     */
    private int processWebtoonTriggerRequests(List<WebtoonTriggerRequest> requests) {
        log.info("웹툰 트리거 요청 처리 시작: {} 개의 웹툰", requests.size());

        List<WebtoonCrawItem> webtoonCrawItems = requests.stream()
                .map(this::mapToWebtoonCrawItem)
                .collect(Collectors.toList());

        webtoonUpdatePublisher.sendWebtoonUpdateRequest(webtoonCrawItems);
        
        log.info("SQS 메시지 전송 완료: {} 개의 웹툰", webtoonCrawItems.size());
        
        return webtoonCrawItems.size();
    }

    /**
     * 단일 WebtoonTriggerRequest를 WebtoonCrawItem으로 매핑합니다.
     */
    private WebtoonCrawItem mapToWebtoonCrawItem(WebtoonTriggerRequest request) {
        try {
            return WebtoonCrawItem.builder()
                .id(Long.valueOf(request.getId()))
                .platform(request.getPlatform())
                .url(request.getWebtoon_url())
                .build();
        } catch (NumberFormatException e) {
            log.error("잘못된 ID 형식: {}", request.getId());
            throw new BadRequestException("잘못된 ID 형식: " + request.getId());
        }
    }
    
    @Override
    public TaskType getSupportedTaskType() {
        return TaskType.WEBTOON_ADMIN_TRIGGER;
    }
    
    @Override
    public String getExecutorName() {
        return "AdminTriggerBusinessExecutor";
    }
} 