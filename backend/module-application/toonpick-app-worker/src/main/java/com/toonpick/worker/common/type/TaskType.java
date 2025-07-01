package com.toonpick.worker.common.type;

/**
 * 작업 타입 열거형
 * 워커 애플리케이션에서 처리하는 다양한 작업 타입들을 정의
 */
public enum TaskType {
    
    // 웹툰 관련 작업
    WEBTOON_EPISODE_UPDATE("WEBTOON", "웹툰 에피소드 업데이트"),
    WEBTOON_STATUS_UPDATE("WEBTOON", "웹툰 상태 업데이트"),
    WEBTOON_INFO_UPDATE("WEBTOON", "웹툰 정보 업데이트"),
    WEBTOON_UPDATE("WEBTOON", "웹툰 업데이트"),
    WEBTOON_ADMIN_TRIGGER("WEBTOON", "웹툰 관리자 트리거"),
    WEBTOON_NEW_CRAWL("WEBTOON", "신규 웹툰 크롤링"),
    WEBTOON_ALL_CRAWL("WEBTOON", "전체 웹툰 크롤링"),
    
    // 통계 관련 작업
    STATISTICS_AGGREGATION("STATISTICS", "통계 집계"),
    STATISTICS_UPDATE("STATISTICS", "통계 업데이트"),
    RANKING_UPDATE("STATISTICS", "랭킹 업데이트"),
    
    // 추천 시스템 관련 작업
    RECOMMENDATION_UPDATE("RECOMMENDATION", "추천 시스템 업데이트"),
    RECOMMENDATION_MODEL_TRAINING("RECOMMENDATION", "추천 모델 학습"),
    RECOMMENDATION_CACHE_UPDATE("RECOMMENDATION", "추천 캐시 업데이트"),
    
    // 회원 관련 작업
    MEMBER_PREFERENCE_UPDATE("MEMBER", "회원 선호도 업데이트"),
    MEMBER_ACTIVITY_ANALYSIS("MEMBER", "회원 활동 분석"),
    
    // 리뷰 관련 작업
    REVIEW_ANALYSIS("REVIEW", "리뷰 분석"),
    REVIEW_SENTIMENT_ANALYSIS("REVIEW", "리뷰 감정 분석"),
    
    // 데이터 정리 작업
    DATA_CLEANUP("DATA", "데이터 정리"),
    DATA_BACKUP("DATA", "데이터 백업"),
    DATA_ARCHIVE("DATA", "데이터 아카이브"),
    
    // 시스템 관련 작업
    SYSTEM_HEALTH_CHECK("SYSTEM", "시스템 상태 점검"),
    SYSTEM_MAINTENANCE("SYSTEM", "시스템 유지보수"),
    CACHE_REFRESH("SYSTEM", "캐시 새로고침");
    
    private final String domain;
    private final String description;
    
    TaskType(String domain, String description) {
        this.domain = domain;
        this.description = description;
    }
    
    /**
     * 작업 타입의 도메인을 반환합니다.
     * 
     * @return 도메인명
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * 작업 타입의 설명을 반환합니다.
     * 
     * @return 작업 설명
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 특정 도메인의 작업 타입인지 확인합니다.
     * 
     * @param domain 확인할 도메인
     * @return 해당 도메인 여부
     */
    public boolean isDomain(String domain) {
        return this.domain.equals(domain);
    }
    
    /**
     * 웹툰 관련 작업인지 확인합니다.
     * 
     * @return 웹툰 관련 작업 여부
     */
    public boolean isWebtoonTask() {
        return isDomain("WEBTOON");
    }
    
    /**
     * 통계 관련 작업인지 확인합니다.
     * 
     * @return 통계 관련 작업 여부
     */
    public boolean isStatisticsTask() {
        return isDomain("STATISTICS");
    }
    
    /**
     * 추천 시스템 관련 작업인지 확인합니다.
     * 
     * @return 추천 시스템 관련 작업 여부
     */
    public boolean isRecommendationTask() {
        return isDomain("RECOMMENDATION");
    }
    
    /**
     * 시스템 관련 작업인지 확인합니다.
     * 
     * @return 시스템 관련 작업 여부
     */
    public boolean isSystemTask() {
        return isDomain("SYSTEM");
    }
    
    /**
     * 배치 처리가 가능한 작업인지 확인합니다.
     * 
     * @return 배치 처리 가능 여부
     */
    public boolean isBatchProcessable() {
        return isWebtoonTask() || isStatisticsTask() || isRecommendationTask();
    }
    
    /**
     * 실시간 처리가 필요한 작업인지 확인합니다.
     * 
     * @return 실시간 처리 필요 여부
     */
    public boolean isRealTimeRequired() {
        return this == SYSTEM_HEALTH_CHECK || this == CACHE_REFRESH;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name(), domain, description);
    }
} 