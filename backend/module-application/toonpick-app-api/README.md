# TOONPICK API Server

웹툰 추천 플랫폼의 사용자 API 서버입니다. 사용자들이 웹툰을 조회, 검색하고 리뷰를 작성하며 개인화된 추천을 받을 수 있는 REST API를 제공합니다.

## 🏗️ 아키텍처

```
src/main/java/com/toonpick/
├── WebApplication.java              # 메인 애플리케이션 클래스
├── config/                          # 설정 클래스들
├── auth/                           # 인증 관련
│   ├── controller/                 # 인증 컨트롤러
│   ├── service/                    # 인증 서비스
│   ├── request/                    # 인증 요청 DTO
│   ├── response/                   # 인증 응답 DTO
│   └── exception/                  # 인증 예외 처리
├── member/                         # 회원 관리
│   ├── controller/                 # 회원 컨트롤러
│   ├── service/                    # 회원 서비스
│   ├── request/                    # 회원 요청 DTO
│   ├── response/                   # 회원 응답 DTO
│   └── mapper/                     # 회원 매퍼
├── webtoon/                        # 웹툰 관리
│   ├── controller/                 # 웹툰 컨트롤러
│   ├── service/                    # 웹툰 서비스
│   ├── request/                    # 웹툰 요청 DTO
│   ├── response/                   # 웹툰 응답 DTO
│   └── mapper/                     # 웹툰 매퍼
├── review/                         # 리뷰 관리
│   ├── controller/                 # 리뷰 컨트롤러
│   ├── service/                    # 리뷰 서비스
│   ├── request/                    # 리뷰 요청 DTO
│   ├── response/                   # 리뷰 응답 DTO
│   └── mapper/                     # 리뷰 매퍼
└── toon_collection/                # 웹툰 컬렉션
    ├── controller/                 # 컬렉션 컨트롤러
    ├── service/                    # 컬렉션 서비스
    ├── request/                    # 컬렉션 요청 DTO
    ├── response/                   # 컬렉션 응답 DTO
    └── mapper/                     # 컬렉션 매퍼
```

## 🚀 주요 기능

### 1. 인증 (Authentication)
- **OAuth2 소셜 로그인**: Google, Naver 지원
- **JWT 토큰 기반 인증**: Access Token + Refresh Token
- **회원가입/로그인**: 이메일, 소셜 계정 연동
- **토큰 갱신**: 자동 토큰 갱신 API

### 2. 회원 관리 (Member Management)
- **프로필 관리**: 회원 정보 조회/수정
- **웹툰 상호작용**: 좋아요, 읽은 목록, 찜하기
- **개인화 설정**: 선호 장르, 알림 설정
- **활동 내역**: 읽은 웹툰, 작성한 리뷰

### 3. 웹툰 서비스 (Webtoon Service)
- **웹툰 조회**: 목록, 상세, 검색
- **추천 시스템**: 개인화된 웹툰 추천
- **필터링**: 장르, 플랫폼, 상태별 필터
- **정렬**: 인기순, 최신순, 평점순

### 4. 리뷰 시스템 (Review System)
- **리뷰 작성/수정/삭제**: 인증된 사용자만
- **평점 시스템**: 1-5점 평점
- **리뷰 조회**: 웹툰별, 사용자별 리뷰
- **공개/비공개 리뷰**: 사용자 설정 가능

### 5. 웹툰 컬렉션 (Webtoon Collection)
- **즐겨찾기**: 웹툰 즐겨찾기 추가/제거
- **읽은 목록**: 읽은 웹툰 관리
- **컬렉션 생성**: 개인 웹툰 컬렉션
- **공유 기능**: 컬렉션 공유

## 📡 API 엔드포인트

### 인증 API
```
POST   /api/v1/auth/join              # 회원가입
POST   /api/v1/auth/login             # 로그인
POST   /api/v1/auth/refresh           # 토큰 갱신
POST   /api/v1/auth/logout            # 로그아웃
GET    /api/v1/auth/oauth2/google     # Google OAuth2
GET    /api/v1/auth/oauth2/naver      # Naver OAuth2
```

### 회원 API
```
GET    /api/v1/members/profile        # 프로필 조회
PUT    /api/v1/members/profile        # 프로필 수정
DELETE /api/v1/members                # 회원 탈퇴
GET    /api/v1/members/activities     # 활동 내역
```

### 웹툰 API
```
GET    /api/v1/webtoons               # 웹툰 목록
GET    /api/v1/webtoons/{id}          # 웹툰 상세
GET    /api/v1/webtoons/search        # 웹툰 검색
GET    /api/v1/webtoons/recommend     # 추천 웹툰
GET    /api/v1/webtoons/popular       # 인기 웹툰
```

### 리뷰 API
```
GET    /api/v1/webtoons/{id}/reviews  # 리뷰 목록
POST   /api/v1/webtoons/{id}/reviews  # 리뷰 작성
PUT    /api/v1/reviews/{id}           # 리뷰 수정
DELETE /api/v1/reviews/{id}           # 리뷰 삭제
```

### 컬렉션 API
```
GET    /api/v1/collections            # 컬렉션 목록
POST   /api/v1/collections            # 컬렉션 생성
PUT    /api/v1/collections/{id}       # 컬렉션 수정
DELETE /api/v1/collections/{id}       # 컬렉션 삭제
POST   /api/v1/collections/{id}/webtoons/{webtoonId}  # 웹툰 추가
DELETE /api/v1/collections/{id}/webtoons/{webtoonId}  # 웹툰 제거
```

## ⚙️ 설정

### 서버 설정
- **포트**: 8080
- **기본 프로필**: dev
- **파일 업로드**: 최대 10MB

### 데이터베이스 설정
- **MariaDB**: 메인 데이터베이스 (포트 3306)
- **MongoDB**: 웹툰 데이터 (포트 27017)
- **Redis**: 캐시 및 세션 (포트 6380)

### JWT 설정
- **Access Token**: 100분 (6000000ms)
- **Refresh Token**: 100시간 (360000000ms)
- **시크릿 키**: 환경별 설정

### OAuth2 설정
- **Google OAuth2**: 프로필, 이메일 스코프
- **Naver OAuth2**: 이름, 이메일 스코프
- **리다이렉트 URI**: 환경별 설정

### AWS 설정
- **S3**: 파일 스토리지
- **SQS**: 메시지 큐 (비활성화)
- **리전**: 환경별 설정

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
./gradlew :module-application:toonpick-app-api:test

# 단위 테스트만
./gradlew :module-application:toonpick-app-api:test --tests "*unit*"

# 통합 테스트만
./gradlew :module-application:toonpick-app-api:test --tests "*integration*"
```

## 🚀 실행 방법

### 로컬 개발 환경
```bash
# 의존성 설치
./gradlew :module-application:toonpick-app-api:build

# 개발 서버 실행
./gradlew :module-application:toonpick-app-api:bootRun
```

### Docker 환경
```bash
# Docker 이미지 빌드
docker build -t toonpick-api .

# Docker 컨테이너 실행
docker run -p 8080:8080 toonpick-api
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

## 🔐 보안

### 인증
- **JWT 토큰**: Access Token + Refresh Token
- **OAuth2**: Google, Naver 소셜 로그인
- **토큰 만료**: 자동 갱신 메커니즘

### 권한
- **공개 API**: 웹툰 조회, 검색
- **인증 필요**: 리뷰 작성, 컬렉션 관리
- **관리자 권한**: 특정 관리 기능

### 데이터 보호
- **개인정보 암호화**: 민감한 정보 암호화
- **API 요청 제한**: Rate Limiting
- **CORS 설정**: 허용된 도메인만 접근

## 🔄 의존성

### 내부 모듈
- **module-domain**: 모든 도메인 모듈
- **module-internal**: 보안, 웹, 스토리지 모듈
- **module-common**: 공통 유틸리티

### 외부 라이브러리
- **Spring Boot**: 3.2.2
- **Spring Security**: OAuth2, JWT
- **Spring Data JPA**: 데이터 접근
- **Spring Data MongoDB**: 문서 데이터베이스
- **Spring Data Redis**: 캐시
- **AWS SDK**: S3, SQS

## 📝 개발 가이드

### 코드 컨벤션
- **패키지 구조**: `com.toonpick.{domain}.{layer}`
- **클래스 명명**: PascalCase
- **메서드 명명**: camelCase
- **API 버전**: `/api/v1/` 접두사

### API 설계 원칙
- **RESTful**: REST API 설계 원칙 준수
- **HTTP 상태 코드**: 적절한 상태 코드 사용
- **에러 응답**: 일관된 에러 응답 형식
- **페이징**: 대용량 데이터 페이징 처리

### 성능 최적화
- **캐싱**: Redis를 활용한 캐싱
- **페이징**: 대용량 데이터 페이징
- **지연 로딩**: 필요시에만 데이터 로딩
- **인덱싱**: 데이터베이스 인덱스 최적화 