# Exception 정의서


| Exception 클래스명 | 설명 | HTTP Status |
|:---|:---|:---|
| **BadRequestException** | 잘못된 입력 (Validation 실패, 파라미터 오류) | 400 Bad Request |
| **AuthenticationException** | 로그인 실패, 인증 실패 (ex. 토큰 없음/만료) | 401 Unauthorized |
| **AccessDeniedException** | 인증은 됐지만, 권한이 부족함 (ex. 관리자 권한 필요) | 403 Forbidden |
| **EntityNotFoundException** | 요청한 데이터를 찾을 수 없음 (ex. 회원, 게시글 조회 실패) | 404 Not Found |
| **DuplicateResourceException** | 동일 데이터 중복 생성 시도 (ex. 이메일 중복 회원가입) | 409 Conflict |
| **BusinessException** | 도메인/비즈니스 규칙 위반 (ex. 상태 변경 불가 등) | 422 Unprocessable Entity |
| **InternalServerException** | 예기치 못한 서버 시스템 에러 | 500 Internal Server Error |



## ✅ Exception 계층도


```
Exception (최상위)
│
├── RuntimeException (Unchecked Exception)
│   ├── BadRequestException (400)
│   ├── AuthenticationException (401)
│   ├── AccessDeniedException (403)
│   ├── EntityNotFoundException (404)
│   ├── DuplicateResourceException (409)
│   ├── BusinessException (422)
│   └── InternalServerException (500)
│
└── Exception (Checked Exception)
    └── ServiceUnavailableException (503)
```

---

1. **`Exception`** (최상위)
   - 모든 예외의 최상위 클래스.

2. **`RuntimeException`** (Unchecked Exception)
   - 런타임에 발생하는 예외로, 이 예외는 **잡지 않아도 프로그램 실행에 큰 영향은 없습니다.**
   - 대부분의 표준 예외는 이 클래스를 상속받고, Spring에서 관리하는 예외들은 대부분 `RuntimeException`을 상속합니다.
   
3. **`BadRequestException` (400)**
   - 잘못된 입력값에 대한 예외입니다.
   - 예시: 유효성 검사 실패, 파라미터 오류 등.

4. **`AuthenticationException` (401)**
   - 인증 실패 시 발생하는 예외입니다.
   - 예시: 로그인 실패, 토큰 없음/만료 등.

5. **`AccessDeniedException` (403)**
   - 인증은 되었으나, 권한이 부족할 경우 발생하는 예외입니다.
   - 예시: 권한이 필요한 자원에 접근 시도.

6. **`EntityNotFoundException` (404)**
   - 요청한 데이터를 찾을 수 없을 때 발생합니다.
   - 예시: 특정 회원 또는 게시글 조회 실패.

7. **`DuplicateResourceException` (409)**
   - 중복된 자원 생성 시도 시 발생합니다.
   - 예시: 이미 존재하는 이메일로 회원가입 시도.

8. **`BusinessException` (422)**
   - 도메인 규칙 위반 시 발생하는 예외입니다.
   - 예시: 비즈니스 로직에 의한 제한 사항 위반.

9. **`InternalServerException` (500)**
   - 시스템 에러를 나타냅니다.
   - 예시: 데이터베이스 장애, 외부 API 호출 실패 등.

10. **`ServiceUnavailableException` (503)**
    - 외부 서비스나 의존성의 장애로 서비스가 불가능할 때 발생합니다.
    - 예시: DB 장애, Redis 연결 실패 등.

---

## ✅ Exception 상속 및 관리

- **최상위 예외(`Exception`)**는 기본적인 예외 정보를 제공하며,  
- **`RuntimeException`을 상속하는 예외들**은 대부분 애플리케이션에서 발생하는 예외입니다.  
- **`CheckedException`은 서비스 장애나 외부 시스템 의존성 문제**에서 주로 사용됩니다.

---

## 📌 정리

- **계층화된 예외 처리**는 예외 발생 시 구체적인 예외를 추적하고, **일관된 응답을 제공**하는 데 유리합니다.
- 상속 구조를 명확하게 정의함으로써, 예외 발생 위치 및 원인에 따른 **적절한 HTTP Status Code와 메시지를 제공**할 수 있습니다.

---
