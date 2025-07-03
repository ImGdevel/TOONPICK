# TOONPICK Backend

웹툰 추천 및 관리 플랫폼의 백엔드 시스템입니다.

## 프로젝트 구조

```
backend/
├── module-application/          # 애플리케이션 모듈 (실행 가능한 애플리케이션들)
│   ├── toonpick-app-api/       # API 서버 (사용자 API)
│   ├── toonpick-app-admin/     # 관리자 서버 (관리자 API)
│   └── toonpick-app-worker/    # 워커 서버 (배치 작업, 스케줄링)
├── module-domain/              # 도메인 모듈 (비즈니스 로직)
│   ├── domain-common/          # 공통 도메인
│   ├── domain-member/          # 회원 도메인
│   ├── domain-auth/            # 인증 도메인
│   ├── domain-webtoon/         # 웹툰 도메인
│   ├── domain-review/          # 리뷰 도메인
│   ├── domain-toon-collection/ # 웹툰 컬렉션 도메인
│   ├── domain-member-engagement/ # 회원 참여도 도메인
│   ├── domain-report/          # 신고 도메인
│   ├── domain-webtoon-recommend/ # 웹툰 추천 도메인
│   └── domain-analysis/        # 분석 도메인
├── module-internal/            # 내부 모듈 (공통 기능)
│   ├── core-security/          # 보안 핵심 기능
│   ├── core-web/               # 웹 핵심 기능
│   ├── client-storage/         # 스토리지 클라이언트
│   ├── event-sqs/              # SQS 이벤트 처리
│   └── event-webhook/          # 웹훅 이벤트 처리
└── module-common/              # 공통 모듈 (유틸리티, 예외 등)
```

## 애플리케이션 모듈

### 1. toonpick-app-api
- **목적**: 사용자 API 서버
- **포트**: 8080
- **기능**: 
  - 웹툰 조회 및 검색
  - 회원 관리
  - 리뷰 관리
  - 웹툰 컬렉션 관리
- **메인 클래스**: `WebApplication`

### 2. toonpick-app-admin
- **목적**: 관리자 API 서버
- **기능**:
  - 웹툰 등록 및 관리
  - 회원 관리
  - 시스템 모니터링
- **메인 클래스**: `AdminApplication`

### 3. toonpick-app-worker
- **목적**: 배치 작업 및 스케줄링 서버
- **기능**:
  - 웹툰 데이터 수집
  - 추천 알고리즘 실행
  - 정기적인 데이터 처리
- **메인 클래스**: `WorkerApplication`
- **특징**: `@EnableScheduling` 활성화

## 🏛️ 도메인 모듈

### Core Domains
- **domain-common**: 공통 엔티티, 공통 기능
- **domain-member**: 회원 관리 (가입, 프로필, 설정)
- **domain-auth**: 인증 및 권한 관리
- **domain-webtoon**: 웹툰 정보 관리 (제목, 작가, 플랫폼, 에피소드)

### Feature Domains
- **domain-review**: 리뷰 및 평점 시스템
- **domain-toon-collection**: 웹툰 컬렉션 (즐겨찾기, 읽은 목록)
- **domain-member-engagement**: 회원 참여도 분석
- **domain-report**: 신고 및 모더레이션
- **domain-webtoon-recommend**: 웹툰 추천 알고리즘
- **domain-analysis**: 데이터 분석 및 통계

## 내부 모듈

### Core Modules
- **core-security**: JWT 인증, 권한 관리, 보안 필터
- **core-web**: 웹 공통 기능, 응답 처리, 예외 처리

### Infrastructure Modules
- **client-storage**: AWS S3 스토리지 클라이언트
- **event-sqs**: AWS SQS 메시지 큐 처리
- **event-webhook**: 외부 웹훅 이벤트 처리

## 공통 모듈

- **utils**: 유틸리티 클래스 (UUID, ShortId 등)
- **exception**: 공통 예외 클래스
- **constants**: 상수 정의
- **type**: 공통 타입 정의

## 기술 스택

### Core
- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Data JPA**
- **Gradle**

### Database
- **MongoDB 6.0** (메인 데이터베이스)
- **Redis 6.0** (캐시, 세션)

### Infrastructure
- **AWS S3** (파일 스토리지)
- **AWS SQS** (메시지 큐)
- **Docker** (컨테이너화)

### Testing
- **JUnit 5**
- **Mockito**
- **H2 Database** (테스트용)


## 🧪 테스트 구조

```
src/test/java/com/toonpick/worker/
├── unit/                    # 단위 테스트
│   └── service/            # 서비스 계층 단위 테스트
├── integration/            # 통합 테스트
│   └── service/            # 서비스 계층 통합 테스트
└── config/                 # 테스트 설정 및 커스텀 어노테이션
    ├── TestDataSourceConfig.java
    ├── UnitTest.java
    └── IntegrationTest.java
```

### 커스텀 어노테이션
- **@UnitTest**: 단위 테스트용 (Mockito 포함)
- **@IntegrationTest**: 통합 테스트용 (Spring Boot Test 포함)


## 📁 모듈 의존성

### Application Dependencies
- **toonpick-app-api**: 모든 도메인 + 내부 모듈
- **toonpick-app-admin**: 핵심 도메인 + 웹/보안 모듈
- **toonpick-app-worker**: 핵심 도메인 + SQS/웹훅 모듈

### Domain Dependencies
- 모든 도메인은 `module-common`에 의존
- 도메인 간 의존성은 최소화하여 설계

## 🔐 환경 설정

- **개발**: `application-dev.yml`
- **운영**: `application-prod.yml`
- **테스트**: `application-test.yml`


## 📊 모니터링

- **로그**: `./logs/` 디렉토리에 저장
- **헬스체크**: MongoDB 연결 상태 확인
- **메트릭**: Spring Boot Actuator 활용

## 🤝 개발 가이드

### 코드 컨벤션
- **패키지**: `com.toonpick.{module}.{layer}`
- **클래스**: PascalCase
- **메서드**: camelCase
- **상수**: UPPER_SNAKE_CASE

### 테스트 작성
- **단위 테스트**: `@UnitTest` 어노테이션 사용
- **통합 테스트**: `@IntegrationTest` 어노테이션 사용
- **Given-When-Then** 패턴 준수

### 모듈 추가 시
1. `settings.gradle`에 모듈 추가
2. `build.gradle`에 의존성 설정
3. 적절한 패키지 구조 생성
4. 테스트 코드 작성
