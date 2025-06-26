# 테스트 구조 가이드

## 디렉토리 구조

```
src/test/java/com/toonpick/
├── unit/                    # 단위 테스트
│   └── service/            # 서비스 계층 단위 테스트
├── integration/            # 통합 테스트
│   └── service/            # 서비스 계층 통합 테스트
└── config/                 # 테스트 설정
    └── TestDataSourceConfig.java
```

## 테스트 분류

### 1. 단위 테스트 (`unit/`)
- **목적**: 개별 컴포넌트의 로직을 격리하여 테스트
- **특징**: 
  - Mock 객체 사용
  - 빠른 실행 속도
  - 외부 의존성 없음
- **위치**: `unit/service/`
- **예시**: `WebtoonRegistrationServiceTest.java`, `WebtoonEpisodeUpdateServiceTest.java`

### 2. 통합 테스트 (`integration/`)
- **목적**: 여러 컴포넌트 간의 상호작용 테스트
- **특징**:
  - 실제 데이터베이스 사용 (H2 인메모리)
  - Spring Context 로딩
  - 실제 의존성 주입
- **위치**: `integration/service/`
- **예시**: `WebtoonRegistrationServiceIntegrationTest.java`

### 3. 테스트 설정 (`config/`)
- **목적**: 테스트용 설정 클래스들
- **특징**:
  - H2 데이터베이스 설정
  - JPA 설정
  - 트랜잭션 관리 설정
- **위치**: `config/`
- **예시**: `TestDataSourceConfig.java`

## 테스트 실행 방법

### 단위 테스트만 실행
```bash
./gradlew test --tests "*unit*"
```

### 통합 테스트만 실행
```bash
./gradlew test --tests "*integration*"
```

### 전체 테스트 실행
```bash
./gradlew test
```

## 네이밍 컨벤션

- **단위 테스트**: `{ClassName}Test.java`
- **통합 테스트**: `{ClassName}IntegrationTest.java`
- **설정 클래스**: `{Purpose}Config.java`

## 테스트 작성 가이드

### 단위 테스트 작성 시
1. `@ExtendWith(MockitoExtension.class)` 사용
2. `@Mock`으로 의존성 모킹
3. `@InjectMocks`로 테스트 대상 주입
4. Given-When-Then 패턴 사용

### 통합 테스트 작성 시
1. `@SpringBootTest` 사용
2. `@ActiveProfiles("test")` 설정
3. `@Import(TestDataSourceConfig.class)` 추가
4. 실제 데이터베이스 상태 검증 