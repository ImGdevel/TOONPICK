# TOONPICK Worker Server

웹툰 추천 플랫폼의 배치 작업 및 스케줄링 서버입니다. 웹툰 데이터 수집, 추천 알고리즘 실행, 정기적인 데이터 처리를 담당합니다.

## 🏗️ 아키텍처

```
src/main/java/com/toonpick/worker/
├── WorkerApplication.java            # 메인 애플리케이션 클래스 (@EnableScheduling)
├── config/                           # 설정 클래스들
├── domain/                           # 도메인 로직
│   ├── entity/                       # 엔티티
│   ├── repository/                   # 리포지토리
│   └── service/                      # 도메인 서비스
├── application/                      # 애플리케이션 로직
│   ├── controller/                   # 관리자 컨트롤러
│   ├── service/                      # 애플리케이션 서비스
│   ├── dto/                          # 데이터 전송 객체
│   └── exception/                    # 예외 처리
├── infrastructure/                   # 인프라스트럭처
│   ├── client/                       # 외부 클라이언트
│   ├── config/                       # 인프라 설정
│   └── repository/                   # 외부 저장소
├── task/                             # 배치 작업
│   ├── executor/                     # 작업 실행기
│   ├── coordinator/                  # 작업 조율자
│   └── context/                      # 작업 컨텍스트
├── scheduler/                        # 스케줄러
├── listener/                         # 이벤트 리스너
├── publisher/                        # 이벤트 발행자
├── mapper/                           # 데이터 매퍼
├── dto/                              # 데이터 전송 객체
└── common/                           # 공통 기능
```

## 🚀 주요 기능

### 1. 웹툰 데이터 수집 (Webtoon Data Collection)
- **자동 수집**: 정기적인 웹툰 데이터 수집
- **플랫폼별 수집**: Naver, Kakao 등 플랫폼별 데이터 수집
- **에피소드 업데이트**: 새로운 에피소드 정보 수집
- **메타데이터 수집**: 작가, 장르, 평점 등 메타데이터 수집
- **이미지 다운로드**: 썸네일, 커버 이미지 다운로드

### 2. 추천 알고리즘 실행 (Recommendation Algorithm)
- **개인화 추천**: 사용자별 맞춤 추천 생성
- **콘텐츠 기반 추천**: 장르, 작가 기반 추천
- **협업 필터링**: 사용자 행동 기반 추천
- **실시간 추천**: 실시간 추천 점수 계산
- **A/B 테스트**: 추천 알고리즘 성능 테스트

### 3. 데이터 처리 및 분석 (Data Processing & Analysis)
- **데이터 정제**: 수집된 데이터 정제 및 검증
- **통계 분석**: 웹툰 인기도, 트렌드 분석
- **사용자 행동 분석**: 사용자 활동 패턴 분석
- **성능 최적화**: 데이터베이스 성능 최적화
- **데이터 백업**: 중요 데이터 자동 백업

### 4. 스케줄링 작업 (Scheduled Tasks)
- **일일 작업**: 매일 실행되는 정기 작업
- **주간 작업**: 주간 통계 및 분석 작업
- **월간 작업**: 월간 리포트 생성 작업
- **실시간 작업**: 실시간 데이터 처리 작업
- **장애 복구**: 장애 발생 시 자동 복구 작업

### 5. 메시지 큐 처리 (Message Queue Processing)
- **SQS 메시지 처리**: AWS SQS 메시지 큐 처리
- **비동기 작업**: 시간이 오래 걸리는 작업 비동기 처리
- **작업 상태 관리**: 작업 진행 상황 추적
- **에러 처리**: 메시지 처리 실패 시 재시도
- **모니터링**: 메시지 큐 상태 모니터링

## 📡 API 엔드포인트

### 관리자 API
```
GET    /api/v1/worker/health              # 워커 상태 확인
GET    /api/v1/worker/tasks               # 작업 목록 조회
GET    /api/v1/worker/tasks/{id}          # 작업 상세 조회
POST   /api/v1/worker/tasks/execute       # 작업 수동 실행
PUT    /api/v1/worker/tasks/{id}/stop     # 작업 중지
GET    /api/v1/worker/statistics          # 작업 통계
```

### 웹툰 수집 API
```
POST   /api/v1/worker/webtoons/collect    # 웹툰 수집 시작
GET    /api/v1/worker/webtoons/status     # 수집 상태 확인
PUT    /api/v1/worker/webtoons/update     # 웹툰 정보 업데이트
DELETE /api/v1/worker/webtoons/cleanup    # 오래된 데이터 정리
```

### 추천 시스템 API
```
POST   /api/v1/worker/recommendations/generate # 추천 생성
GET    /api/v1/worker/recommendations/status   # 추천 상태 확인
PUT    /api/v1/worker/recommendations/refresh  # 추천 새로고침
```

## ⚙️ 설정

### 서버 설정
- **포트**: 8082
- **기본 프로필**: dev
- **스케줄링**: `@EnableScheduling` 활성화

### 데이터베이스 설정
- **MariaDB (Data)**: 메인 데이터베이스 (포트 3306)
- **MariaDB (Meta)**: 메타데이터 데이터베이스 (포트 3306)
- **MongoDB**: 웹툰 데이터 (포트 27017)
- **Redis**: 캐시 및 세션 (포트 6380)

### 배치 설정
- **배치 작업 활성화**: `spring.batch.job.enabled=true`
- **스키마 초기화**: `spring.batch.jdbc.initialize-schema=always`
- **작업 병렬 처리**: 멀티스레드 작업 처리

### AWS 설정
- **S3**: 파일 스토리지
- **SQS**: 메시지 큐 (비활성화)
- **리전**: 환경별 설정

### SQS 큐 설정
- **웹툰 생성 요청**: `AWS_SQS_WEBTOON_CREATE_REQUEST`
- **웹툰 업데이트 요청**: `AWS_SQS_WEBTOON_UPDATE_REQUEST`
- **웹툰 생성 완료**: `AWS_SQS_WEBTOON_CREATE_COMPLETE`
- **웹툰 업데이트 완료**: `AWS_SQS_WEBTOON_UPDATE_COMPLETE`

## 🔄 스케줄링

### 일일 작업 (Daily Tasks)
```java
@Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시
public void dailyWebtoonCollection() {
    // 웹툰 데이터 수집
}

@Scheduled(cron = "0 30 2 * * *") // 매일 새벽 2시 30분
public void dailyRecommendationGeneration() {
    // 추천 알고리즘 실행
}
```

### 주간 작업 (Weekly Tasks)
```java
@Scheduled(cron = "0 0 3 * * MON") // 매주 월요일 새벽 3시
public void weeklyDataAnalysis() {
    // 주간 데이터 분석
}
```

### 월간 작업 (Monthly Tasks)
```java
@Scheduled(cron = "0 0 4 1 * *") // 매월 1일 새벽 4시
public void monthlyReportGeneration() {
    // 월간 리포트 생성
}
```

## 🧪 테스트

### 테스트 구조
```
src/test/java/com/toonpick/worker/
├── unit/                    # 단위 테스트
│   ├── service/            # 서비스 테스트
│   ├── task/               # 작업 테스트
│   └── mapper/             # 매퍼 테스트
├── integration/            # 통합 테스트
│   ├── service/            # 서비스 통합 테스트
│   └── task/               # 작업 통합 테스트
└── config/                 # 테스트 설정
    ├── TestDataSourceConfig.java
    ├── UnitTest.java
    └── IntegrationTest.java
```

### 테스트 실행
```bash
# 전체 테스트
./gradlew :module-application:toonpick-app-worker:test

# 단위 테스트만
./gradlew :module-application:toonpick-app-worker:test --tests "*unit*"

# 통합 테스트만
./gradlew :module-application:toonpick-app-worker:test --tests "*integration*"
```

## 🚀 실행 방법

### 로컬 개발 환경
```bash
# 의존성 설치
./gradlew :module-application:toonpick-app-worker:build

# 개발 서버 실행
./gradlew :module-application:toonpick-app-worker:bootRun
```

### Docker 환경
```bash
# Docker 이미지 빌드
docker build -t toonpick-worker .

# Docker 컨테이너 실행
docker run -p 8082:8082 toonpick-worker
```

## 📊 모니터링

### 헬스체크
- **엔드포인트**: `/actuator/health`
- **상세 정보**: `/actuator/health/details`
- **정보**: `/actuator/info`

### 로깅
- **로그 파일**: `logs/dev-application.log`
- **최대 크기**: 10MB
- **로그 레벨**: DEBUG (개발 환경)
- **작업 로그**: 배치 작업 진행 상황 로깅

### 메트릭
- **작업 메트릭**: 작업 실행 시간, 성공률
- **시스템 메트릭**: CPU, 메모리, 디스크 사용량
- **비즈니스 메트릭**: 수집된 웹툰 수, 추천 생성 수

## 🔄 의존성

### 내부 모듈
- **module-domain**: 핵심 도메인 모듈
- **module-internal**: SQS, 웹훅 모듈
- **module-common**: 공통 유틸리티

### 외부 라이브러리
- **Spring Boot**: 3.2.2
- **Spring Batch**: 배치 작업 처리
- **Spring Data JPA**: 데이터 접근
- **Spring Data MongoDB**: 문서 데이터베이스
- **Spring Data Redis**: 캐시
- **AWS SDK**: S3, SQS

## 📝 개발 가이드

### 코드 컨벤션
- **패키지 구조**: `com.toonpick.worker.{layer}`
- **클래스 명명**: PascalCase
- **메서드 명명**: camelCase
- **작업 명명**: `{Purpose}Task`

### 배치 작업 설계 원칙
- **작업 분리**: 각 작업은 독립적으로 실행 가능
- **재시작 가능**: 장애 발생 시 재시작 가능
- **모니터링**: 작업 진행 상황 추적
- **에러 처리**: 적절한 에러 처리 및 로깅

### 성능 최적화
- **병렬 처리**: 멀티스레드 작업 처리
- **배치 크기**: 적절한 배치 크기 설정
- **메모리 관리**: 대용량 데이터 처리 시 메모리 최적화
- **인덱싱**: 데이터베이스 인덱스 최적화

## 🛡️ 운영 가이드

### 작업 모니터링
- **작업 상태**: 실시간 작업 상태 모니터링
- **성능 지표**: 작업 실행 시간, 처리량
- **에러 알림**: 작업 실패 시 즉시 알림
- **리소스 모니터링**: CPU, 메모리 사용량

### 장애 대응
- **자동 재시작**: 작업 실패 시 자동 재시작
- **수동 개입**: 필요시 수동 작업 실행
- **데이터 복구**: 작업 실패 시 데이터 복구
- **롤백**: 문제 발생 시 이전 상태로 롤백

### 확장성
- **수평 확장**: 여러 워커 인스턴스 실행
- **작업 분산**: 작업을 여러 인스턴스에 분산
- **로드 밸런싱**: 작업 부하 분산
- **자동 스케일링**: 부하에 따른 자동 확장 