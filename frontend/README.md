# 🧾 Frontend Naming & Directory Convention Guide

본 문서는 React + TypeScript 프로젝트에서 사용하는 명명 및 디렉터리 구조에 대한 컨벤션을 정의합니다.  
코드 일관성을 유지하고 협업 효율을 높이기 위함입니다.

<br>

---

## 1. 디렉터리 구조

| 디렉터리       | 설명                                      |
| -------------- | ----------------------------------------- |
| `components/`  | 재사용 가능한 UI 컴포넌트 모음             |
| `pages/`       | 라우트 단위 페이지 컴포넌트               |
| `hooks/`       | 커스텀 훅 모음                            |
| `services/`    | API 호출 또는 비즈니스 로직 분리          |
| `types/`       | 공통 타입 정의                           |
| `utils/`       | 유틸 함수 모음                           |

폴더명은 **kebab-case** 사용

<br>

## 2. 파일명 규칙

| 유형            | 컨벤션                           | 예시                     |
| --------------- | -------------------------------- | ------------------------ |
| 일반 파일       | `kebab-case`                     | `user-service.ts`         |
| 컴포넌트 파일    | `kebab-case.tsx` 혹은 `PascalCase.tsx` | `user-card.tsx` or `UserCard.tsx` |
| 스타일 파일      | 컴포넌트와 동일한 이름 + `.module.css` | `user-card.module.css`    |

<br>

## 3. 네이밍 컨벤션

### 네이밍 컨벤션

| 항목             | 규칙                               | 예시                     |
| ---------------- | ---------------------------------- | ------------------------ |
| 변수명           | `camelCase`                        | `userName`, `isLoading`   |
| 함수명           | `camelCase`                        | `handleClick`, `fetchUserData` |
| 클래스명         | `PascalCase`                       | `UserService`, `UserCard` |
| 컴포넌트명       | `PascalCase`                       | `UserCard`, `WebtoonList` |
| 파일/폴더명      | `kebab-case`                       | `user-card.tsx`, `user-service.ts` |
| 상태 변수        | `camelCase` + 상태 prefix         | `isOpen`, `hasError`      |
