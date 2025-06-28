package com.toonpick.worker.application.service;

import com.toonpick.worker.common.type.TaskType;
import com.toonpick.worker.task.coordinator.TaskCoordinator;
import com.toonpick.worker.task.strategy.TaskContext;
import com.toonpick.worker.dto.request.WebtoonTriggerRequest;
import com.toonpick.worker.dto.response.WebtoonTriggerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 관리자 Worker 서비스
 * 관리자가 요청한 작업들을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminWorkerService {
    
    private final TaskCoordinator taskCoordinator;
    
    /**
     * 웹툰 트리거 요청을 처리합니다.
     * 
     * @param requests 웹툰 트리거 요청 리스트
     * @return 처리 결과
     */
    public WebtoonTriggerResponse processWebtoonTriggerRequests(List<WebtoonTriggerRequest> requests) {
        log.info("웹툰 트리거 요청 처리 시작: {} 개의 웹툰", requests.size());
        
        // TaskContext 생성
        String taskId = UUID.randomUUID().toString();
        Map<String, Object> data = new HashMap<>();
        data.put("requests", requests);
        
        TaskContext context = TaskContext.builder()
                .taskId(taskId)
                .taskType(TaskType.WEBTOON_ADMIN_TRIGGER)
                .data(data)
                .build();
        
        try {
            // TaskCoordinator를 통해 작업 실행
            taskCoordinator.coordinate(TaskType.WEBTOON_ADMIN_TRIGGER, context);
            
            log.info("웹툰 트리거 요청 처리 완료: taskId={}, 웹툰 수={}", taskId, requests.size());
            
            return WebtoonTriggerResponse.builder()
                    .success(true)
                    .message("웹툰 트리거 요청이 성공적으로 처리되었습니다.")
                    .taskId(taskId)
                    .processedCount(requests.size())
                    .build();
                    
        } catch (Exception e) {
            log.error("웹툰 트리거 요청 처리 실패: taskId={}", taskId, e);
            
            return WebtoonTriggerResponse.builder()
                    .success(false)
                    .message("웹툰 트리거 요청 처리 중 오류가 발생했습니다: " + e.getMessage())
                    .taskId(taskId)
                    .processedCount(0)
                    .build();
        }
    }
} 