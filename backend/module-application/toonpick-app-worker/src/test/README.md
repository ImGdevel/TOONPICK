# 테스트 구조 가이드

## 디렉토리 구조

```
src/test/java/com/toonpick/worker/
├── unit/                    # 단위 테스트
│   └── service/            # 서비스 계층 단위 테스트
│       ├── WebtoonRegistrationServiceTest.java
│       └── WebtoonEpisodeUpdateServiceTest.java
├── integration/            # 통합 테스트
│   └── service/            # 서비스 계층 통합 테스트
│       └── WebtoonRegistrationServiceIntegrationTest.java
└── config/                 # 테스트 설정 및 커스텀 어노테이션
    ├── TestDataSourceConfig.java
    ├── UnitTest.java
    └── IntegrationTest.java
```

## 테스트 분류

### 1. 단위 테스트 (`unit/`)
- **목적**: 개별 컴포넌트의 로직을 격리하여 테스트
- **특징**: 
  - Mock 객체 사용
  - 빠른 실행 속도
  - 외부 의존성 없음
- **위치**: `unit/service/`
- **어노테이션**: `@UnitTest`
- **예시**: `WebtoonRegistrationServiceTest.java`, `WebtoonEpisodeUpdateServiceTest.java`

### 2. 통합 테스트 (`integration/`)
- **목적**: 여러 컴포넌트 간의 상호작용 테스트
- **특징**:
  - 실제 데이터베이스 사용 (H2 인메모리)
  - Spring Context 로딩
  - 실제 의존성 주입
- **위치**: `integration/service/`
- **어노테이션**: `@IntegrationTest`
- **예시**: `WebtoonRegistrationServiceIntegrationTest.java`

### 3. 테스트 설정 (`config/`)
- **목적**: 테스트용 설정 클래스들 및 커스텀 어노테이션
- **특징**:
  - H2 데이터베이스 설정
  - JPA 설정
  - 트랜잭션 관리 설정
  - 테스트 타입별 커스텀 어노테이션
- **위치**: `config/`
- **파일들**: 
  - `TestDataSourceConfig.java` - 테스트용 데이터소스 설정
  - `UnitTest.java` - 단위 테스트용 커스텀 어노테이션
  - `IntegrationTest.java` - 통합 테스트용 커스텀 어노테이션

## 커스텀 어노테이션

### @UnitTest
```java
@UnitTest
class MyServiceTest {
    // 단위 테스트 코드
}
```
- `@ExtendWith(MockitoExtension.class)` 포함
- `@Tag("UnitTest")` 포함

### @IntegrationTest
```java
@IntegrationTest
class MyServiceIntegrationTest {
    // 통합 테스트 코드
}
```
- `@SpringBootTest` 포함
- `@ActiveProfiles("test")` 포함
- `@Import(TestDataSourceConfig.class)` 포함
- `@Tag("IntegrationTest")` 포함

## 테스트 실행 방법

### 단위 테스트만 실행
```bash
./gradlew test --tests "*unit*"
# 또는
./gradlew test --tests "*UnitTest*"
```

### 통합 테스트만 실행
```bash
./gradlew test --tests "*integration*"
# 또는
./gradlew test --tests "*IntegrationTest*"
```

### 전체 테스트 실행
```bash
./gradlew test
```

## 네이밍 컨벤션

- **단위 테스트**: `{ClassName}Test.java`
- **통합 테스트**: `{ClassName}IntegrationTest.java`
- **설정 클래스**: `{Purpose}Config.java`
- **커스텀 어노테이션**: `{Type}Test.java`

## 테스트 작성 가이드

### 단위 테스트 작성 시
1. `@UnitTest` 어노테이션 사용
2. `@Mock`으로 의존성 모킹
3. `@InjectMocks`로 테스트 대상 주입
4. Given-When-Then 패턴 사용

```java
@UnitTest
class MyServiceTest {
    @Mock
    private MyRepository repository;
    
    @InjectMocks
    private MyService service;
    
    @Test
    void testMethod() {
        // Given
        // When
        // Then
    }
}
```

### 통합 테스트 작성 시
1. `@IntegrationTest` 어노테이션 사용
2. 실제 데이터베이스 상태 검증
3. `@Autowired`로 실제 빈 주입

```java
@IntegrationTest
class MyServiceIntegrationTest {
    @Autowired
    private MyService service;
    
    @Autowired
    private MyRepository repository;
    
    @Test
    void testIntegration() {
        // 실제 데이터베이스와 상호작용 테스트
    }
}
```

## 장점

1. **명확한 분리**: 단위/통합 테스트가 명확히 구분됨
2. **커스텀 어노테이션**: 반복적인 설정 코드 제거
3. **선택적 실행**: 테스트 유형별로 선택적 실행 가능
4. **유지보수성**: 테스트 목적에 따른 구조화로 유지보수 용이
5. **확장성**: 새로운 테스트 유형 추가 시 구조적 확장 가능
6. **팀 협업**: 명확한 구조로 팀원들이 쉽게 이해하고 따라할 수 있음 