# Git Commit Message Convention

본 프로젝트는 일관된 커밋 메시지를 위해 **Conventional Commits** 규칙을 따릅니다.

## 1. 커밋 메시지 구조

```
<type>(optional scope): <subject>

<body>

<footer>
```

### 필수 요소
- `type`: 커밋의 종류를 나타냅니다.
- `subject`: 한 줄로 요약된 커밋 메시지 (명령문, 첫 글자 소문자, 마침표 없음)

### 선택 요소
- `scope`: 영향받은 범위 (ex. module, component, file 등)
- `body`: 변경한 이유나 상세 설명
- `footer`: 이슈 번호 참조, 브레이킹 체인지 표시

---

## 2. 커밋 타입 (type)

| 타입       | 설명 |
|------------|------|
| `feat`     | 새로운 기능 추가 |
| `fix`      | 버그 수정 |
| `docs`     | 문서 수정 |
| `style`    | 코드 포맷팅, 세미콜론 누락 등 (기능/로직 변경 없음) |
| `refactor` | 코드 리팩토링 (기능 변경 없음) |
| `test`     | 테스트 코드 추가/수정 |
| `chore`    | 빌드 업무, 패키지 매니저 설정 등 기타 작업 |
| `perf`     | 성능 개선 |
| `ci`       | CI 설정 수정 |
| `build`    | 빌드 시스템 혹은 외부 의존성 수정 |
| `revert`   | 이전 커밋 되돌리기 |

---

## 3. 예시

### 기능 추가
```
feat(user): add signup endpoint
```

### 버그 수정
```
fix(auth): handle null token exception
```

### 이슈 참조 및 브레이킹 체인지
```
feat(auth): migrate to OAuth2 login

BREAKING CHANGE: 기존 로그인 방식 제거됨
Closes #42
```
