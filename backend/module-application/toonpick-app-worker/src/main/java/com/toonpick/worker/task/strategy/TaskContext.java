package com.toonpick.worker.task.strategy;

import com.toonpick.worker.common.type.TaskType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 작업 실행 컨텍스트
 */
@Getter
@Setter
@Builder
public class TaskContext {
    
    /**
     * 작업 ID
     */
    private String taskId;
    
    /**
     * 작업 타입
     */
    private TaskType taskType;
    
    /**
     * 작업 시작 시간
     */
    private LocalDateTime startTime;
    
    /**
     * 작업 파라미터
     */
    private Map<String, Object> parameters;
    
    /**
     * 작업 데이터 (키-값 쌍으로 저장)
     */
    private Map<String, Object> data;
    
    /**
     * 배치 크기
     */
    private Integer batchSize;
    
    /**
     * 재시도 횟수
     */
    private Integer retryCount;
    
    /**
     * 작업 우선순위
     */
    private Integer priority;
    
    /**
     * 작업 메타데이터
     */
    private Map<String, String> metadata;
    
    /**
     * 데이터에서 특정 키의 값을 가져옵니다.
     * 
     * @param key 데이터 키
     * @return 데이터 값
     */
    public Object getData(String key) {
        return data != null ? data.get(key) : null;
    }
    
    /**
     * 데이터에서 특정 키의 값을 안전하게 가져옵니다.
     * 
     * @param key 데이터 키
     * @param defaultValue 기본값
     * @return 데이터 값 또는 기본값
     */
    public Object getData(String key, Object defaultValue) {
        Object value = getData(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 데이터에서 특정 키의 리스트를 안전하게 가져옵니다.
     * 
     * @param key 데이터 키
     * @param <T> 리스트 요소 타입
     * @return 리스트 또는 null
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getDataAsList(String key) {
        Object value = getData(key);
        if (value instanceof List<?>) {
            return (List<T>) value;
        }
        return null;
    }
    
    /**
     * 데이터에서 특정 키의 문자열을 가져옵니다.
     * 
     * @param key 데이터 키
     * @return 문자열 값
     */
    public String getDataAsString(String key) {
        Object value = getData(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * 데이터에서 특정 키의 정수를 가져옵니다.
     * 
     * @param key 데이터 키
     * @return 정수 값
     */
    public Integer getDataAsInteger(String key) {
        Object value = getData(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }
    
    /**
     * 데이터에 값을 설정합니다.
     * 
     * @param key 데이터 키
     * @param value 데이터 값
     */
    public void setData(String key, Object value) {
        if (data == null) {
            data = new java.util.HashMap<>();
        }
        data.put(key, value);
    }
} 