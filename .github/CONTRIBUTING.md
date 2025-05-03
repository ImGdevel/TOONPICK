# Contributing Guide

이 프로젝트에 기여하려면 아래 규칙을 지켜주세요.

## Commit Message Convention

- 형식: `<type>(optional scope): <subject>`
- 타입:
  - feat: 새로운 기능
  - fix: 버그 수정
  - docs: 문서 수정
  - style: 코드 포맷팅 (비즈니스 로직 변경 없음)
  - refactor: 코드 리팩토링 (동작 변경 없음)
  - perf: 성능 개선
  - test: 테스트 코드 추가
  - chore: 빌드 업무, 패키지 매니저 설정 등
  - revert: 이전 커밋 되돌리기

- 예시:

```
feat(auth): 로그인 기능 추가 
```

```
fix(api): 잘못된 응답 코드 수정
```

## Branch Strategy

- `main`: 제품 릴리즈 버전
- `develop`: 개발 버전
- `feature/*`: 기능 개발 브랜치

## Pull Request Rules

- PR 제목: `TOONPICK : 작업 내용 요약`
- Conflict 발생 시 직접 해결


## 기타 주의사항

- PR 하나당 하나의 명확한 기능만 다루세요
- 문서도 업데이트하는 걸 잊지 마세요
