# TOONPICK Admin Server

웹툰 추천 플랫폼의 관리자 API 서버입니다. 웹툰 등록, 회원 관리, 시스템 모니터링 등 관리자 기능을 제공합니다.

## 🏗️ 아키텍처

```
src/main/java/com/toonpick/
├── AdminApplication.java             # 메인 애플리케이션 클래스
├── config/                           # 설정 클래스들
├── controller/                       # 관리자 컨트롤러
│   ├── AdminWebtoonController.java   # 웹툰 관리 컨트롤러
│   └── AdminWebtoonReviewController.java # 리뷰 관리 컨트롤러
├── service/                          # 관리자 서비스
│   ├── AdminWebtoonService.java      # 웹툰 관리 서비스
│   └── AdminReviewService.java       # 리뷰 관리 서비스
├── mapper/                           # 데이터 매퍼
│   ├── WebtoonMapper.java            # 웹툰 매퍼
│   └── ReviewMapper.java             # 리뷰 매퍼
└── dto/                              # 데이터 전송 객체
    ├── request/                      # 요청 DTO
    └── response/                     # 응답 DTO
```

## 🚀 주요 기능

### 1. 웹툰 관리 (Webtoon Management)
- **웹툰 등록**: 새로운 웹툰 정보 등록
- **웹툰 수정**: 기존 웹툰 정보 수정
- **웹툰 삭제**: 웹툰 정보 삭제
- **웹툰 조회**: 관리자용 웹툰 목록/상세 조회
- **플랫폼 관리**: 웹툰 플랫폼 정보 관리
- **작가 관리**: 웹툰 작가 정보 관리

### 2. 리뷰 관리 (Review Management)
- **리뷰 조회**: 모든 리뷰 목록 조회
- **리뷰 삭제**: 부적절한 리뷰 삭제
- **리뷰 숨김**: 특정 리뷰 숨김 처리
- **리뷰 통계**: 리뷰 통계 정보 조회
- **신고 처리**: 리뷰 신고 처리

### 3. 회원 관리 (Member Management)
- **회원 조회**: 전체 회원 목록 조회
- **회원 상세**: 회원 상세 정보 조회
- **회원 정지**: 부적절한 회원 정지
- **회원 복구**: 정지된 회원 복구
- **회원 통계**: 회원 활동 통계

### 4. 시스템 모니터링 (System Monitoring)
- **시스템 상태**: 서버 상태 모니터링
- **데이터베이스**: DB 연결 상태 확인
- **API 성능**: API 응답 시간 모니터링
- **에러 로그**: 시스템 에러 로그 조회
- **리소스 사용량**: CPU, 메모리 사용량

### 5. 데이터 관리 (Data Management)
- **데이터 백업**: 중요 데이터 백업
- **데이터 복구**: 백업 데이터 복구
- **데이터 정리**: 불필요한 데이터 정리
- **데이터 마이그레이션**: 데이터 구조 변경

## 📡 API 엔드포인트

### 웹툰 관리 API
```
GET    /api/v1/admin/webtoons              # 웹툰 목록 조회
GET    /api/v1/admin/webtoons/{id}         # 웹툰 상세 조회
POST   /api/v1/admin/webtoons              # 웹툰 등록
PUT    /api/v1/admin/webtoons/{id}         # 웹툰 수정
DELETE /api/v1/admin/webtoons/{id}         # 웹툰 삭제
POST   /api/v1/admin/webtoons/{id}/episodes # 에피소드 등록
PUT    /api/v1/admin/webtoons/{id}/status  # 웹툰 상태 변경
```

### 리뷰 관리 API
```
GET    /api/v1/admin/reviews               # 리뷰 목록 조회
GET    /api/v1/admin/reviews/{id}          # 리뷰 상세 조회
DELETE /api/v1/admin/reviews/{id}          # 리뷰 삭제
PUT    /api/v1/admin/reviews/{id}/hide     # 리뷰 숨김
PUT    /api/v1/admin/reviews/{id}/show     # 리뷰 표시
GET    /api/v1/admin/reviews/statistics    # 리뷰 통계
```

### 회원 관리 API
```
GET    /api/v1/admin/members               # 회원 목록 조회
GET    /api/v1/admin/members/{id}          # 회원 상세 조회
PUT    /api/v1/admin/members/{id}/suspend  # 회원 정지
PUT    /api/v1/admin/members/{id}/restore  # 회원 복구
GET    /api/v1/admin/members/statistics    # 회원 통계
```

### 시스템 관리 API
```
GET    /api/v1/admin/system/health         # 시스템 상태
GET    /api/v1/admin/system/logs           # 시스템 로그
GET    /api/v1/admin/system/metrics        # 시스템 메트릭
POST   /api/v1/admin/system/backup         # 데이터 백업
POST   /api/v1/admin/system/restore        # 데이터 복구
```

## ⚙️ 설정

### 서버 설정
- **포트**: 8081
- **기본 프로필**: dev
- **관리자 권한**: 특별 권한 필요

### 데이터베이스 설정
- **MariaDB**: 메인 데이터베이스 (포트 3306)
- **Redis**: 캐시 및 세션 (포트 6380)

### JWT 설정
- **Access Token**: 1분 (60000ms) - 관리자용 짧은 만료
- **Refresh Token**: 1시간 (3600000ms)
- **시크릿 키**: 환경별 설정

### AWS 설정
- **S3**: 파일 스토리지
- **SQS**: 메시지 큐 (비활성화)
- **리전**: 환경별 설정

## 🔐 보안

### 인증 및 권한
- **관리자 전용**: 관리자 계정만 접근 가능
- **JWT 토큰**: 관리자용 토큰
- **IP 제한**: 허용된 IP에서만 접근
- **2FA**: 2단계 인증 지원

### 데이터 보호
- **민감 정보 암호화**: 개인정보 암호화
- **감사 로그**: 모든 관리 작업 로깅
- **백업 보안**: 백업 데이터 암호화

## 🧪 테스트

### 테스트 구조
```
src/test/java/com/toonpick/
├── unit/                    # 단위 테스트
│   ├── controller/         # 컨트롤러 테스트
│   ├── service/            # 서비스 테스트
│   └── mapper/             # 매퍼 테스트
├── integration/            # 통합 테스트
│   ├── controller/         # API 통합 테스트
│   └── service/            # 서비스 통합 테스트
└── config/                 # 테스트 설정
```

### 테스트 실행
```bash
# 전체 테스트
./gradlew :module-application:toonpick-app-admin:test

# 단위 테스트만
./gradlew :module-application:toonpick-app-admin:test --tests "*unit*"

# 통합 테스트만
./gradlew :module-application:toonpick-app-admin:test --tests "*integration*"
```

## 🚀 실행 방법

### 로컬 개발 환경
```bash
# 의존성 설치
./gradlew :module-application:toonpick-app-admin:build

# 개발 서버 실행
./gradlew :module-application:toonpick-app-admin:bootRun
```

### Docker 환경
```bash
# Docker 이미지 빌드
docker build -t toonpick-admin .

# Docker 컨테이너 실행
docker run -p 8081:8081 toonpick-admin
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
- **감사 로그**: 관리 작업 로깅

### 메트릭
- **시스템 메트릭**: CPU, 메모리, 디스크
- **애플리케이션 메트릭**: API 응답 시간, 에러율
- **비즈니스 메트릭**: 웹툰 등록 수, 리뷰 수

## 🔄 의존성

### 내부 모듈
- **module-domain**: 핵심 도메인 모듈
- **module-internal**: 보안, 웹 모듈
- **module-common**: 공통 유틸리티

### 외부 라이브러리
- **Spring Boot**: 3.2.2
- **Spring Security**: JWT 인증
- **Spring Data JPA**: 데이터 접근
- **Spring Data Redis**: 캐시
- **AWS SDK**: S3, SQS

## 📝 개발 가이드

### 코드 컨벤션
- **패키지 구조**: `com.toonpick.{layer}`
- **클래스 명명**: PascalCase
- **메서드 명명**: camelCase
- **API 버전**: `/api/v1/admin/` 접두사

### API 설계 원칙
- **관리자 전용**: 관리자 권한 검증
- **감사 로그**: 모든 작업 로깅
- **배치 처리**: 대용량 데이터 처리
- **에러 처리**: 상세한 에러 메시지

### 성능 최적화
- **캐싱**: Redis를 활용한 캐싱
- **페이징**: 대용량 데이터 페이징
- **비동기 처리**: 시간이 오래 걸리는 작업
- **인덱싱**: 데이터베이스 인덱스 최적화

## 🛡️ 운영 가이드

### 백업 정책
- **자동 백업**: 일일 자동 백업
- **수동 백업**: 필요시 수동 백업
- **백업 검증**: 백업 데이터 무결성 검증

### 장애 대응
- **모니터링**: 24/7 시스템 모니터링
- **알림**: 장애 발생 시 즉시 알림
- **복구 절차**: 표준화된 복구 절차

### 보안 정책
- **접근 제어**: 최소 권한 원칙
- **정기 감사**: 보안 정기 감사
- **업데이트**: 보안 패치 정기 적용 