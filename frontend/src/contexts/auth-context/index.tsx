import React, { createContext, useReducer, useEffect, ReactNode } from 'react';
import AuthService from '@services/auth-service';
import MemberService from '@services/member-service';
import { MemberProfile } from '@models/member';
import TokenManager from '@services/token-manager';

// 인증 상태를 정의하는 인터페이스
interface AuthState {
  isLoggedIn: boolean; // 로그인 여부
  memberProfile: MemberProfile | null; // 회원 프로필 정보
  error: string | null; // 에러 메시지
}

// 인증 액션 타입 정의
type AuthAction =
  | { type: 'LOGIN_SUCCESS'; payload: MemberProfile } // 로그인 성공
  | { type: 'LOGIN_FAILURE'; payload: string } // 로그인 실패
  | { type: 'LOGOUT' } // 로그아웃
  | { type: 'SET_MEMBER_PROFILE'; payload: MemberProfile }; // 회원 프로필 설정

// 초기 상태 정의
const initialState: AuthState = {
  isLoggedIn: false,
  memberProfile: null,
  error: null,
};

// 인증 상태를 관리하는 리듀서 함수
const authReducer = (state: AuthState, action: AuthAction): AuthState => {
  switch (action.type) {
    case 'LOGIN_SUCCESS':
      return { ...state, isLoggedIn: true, memberProfile: action.payload, error: null };
    case 'LOGIN_FAILURE':
      return { ...state, isLoggedIn: false, memberProfile: null, error: action.payload };
    case 'LOGOUT':
      return { ...state, isLoggedIn: false, memberProfile: null, error: null };
    case 'SET_MEMBER_PROFILE':
      return { ...state, memberProfile: action.payload };
    default:
      return state; // 기본 상태 반환
  }
};

// 인증 컨텍스트 타입 정의
interface AuthContextType {
  state: AuthState; // 현재 인증 상태
  login: (username: string, password: string) => Promise<void>; // 로그인 함수
  logout: () => void; // 로그아웃 함수
  setMemberProfile: (profile: MemberProfile) => void; // 회원 프로필 설정 함수
}

// 인증 컨텍스트 생성
const defaultAuthContext: AuthContextType = {
  state: { isLoggedIn: false, memberProfile: null, error: null },
  login: async () => {},
  logout: () => {},
  setMemberProfile: (profile: MemberProfile) => {},
};

export const AuthContext = createContext<AuthContextType>(defaultAuthContext);

// 인증 제공자 프로퍼티 타입 정의
interface AuthProviderProps {
  children: ReactNode; // 자식 컴포넌트
}

// 인증 제공자 컴포넌트
export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState); // 상태와 디스패치 함수 초기화

  // 컴포넌트가 마운트될 때 실행되는 효과
  useEffect(() => {
    const accessToken = TokenManager.getAccessToken(); // 저장된 액세스 토큰 가져오기
    if (accessToken && !TokenManager.isAccessTokenExpired(accessToken)) { // 토큰이 유효한 경우
      const fetchMemberProfile = async () => {
        try {
          const profile = await MemberService.getMemberProfile(); // 회원 프로필 가져오기
          if (profile.data) {
            dispatch({ type: 'LOGIN_SUCCESS', payload: profile.data }); // 로그인 성공
          } else {
            dispatch({ type: 'LOGIN_FAILURE', payload: 'Failed to fetch member profile' }); // 프로필 가져오기 실패
          }
        } catch (error) {
          console.error('Error fetching member profile:', error); // 에러 로그
        }
      };
      fetchMemberProfile(); // 프로필 가져오기 함수 호출
    }
  }, []); // 빈 배열로 의존성 설정하여 컴포넌트가 처음 마운트될 때만 실행

  // 로그인 함수
  const login = async (username: string, password: string) => {
    try {
      const response = await AuthService.login(username, password); // 로그인 요청
      if (response.success) {
        const profile = await MemberService.getMemberProfile(); // 회원 프로필 가져오기
        if (profile.data) {
          dispatch({ type: 'LOGIN_SUCCESS', payload: profile.data }); // 로그인 성공
        } else {
          dispatch({ type: 'LOGIN_FAILURE', payload: 'Failed to fetch member profile' }); // 프로필 가져오기 실패
        }
      } else {
        dispatch({ type: 'LOGIN_FAILURE', payload: response.message || 'Login failed' }); // 로그인 실패
      }
    } catch (error) {
      dispatch({ type: 'LOGIN_FAILURE', payload: 'Login failed due to an unknown error' }); // 에러 처리
    }
  };

  // 로그아웃 함수
  const logout = () => {
    AuthService.logout(); // 로그아웃 요청
    dispatch({ type: 'LOGOUT' }); // 상태 업데이트
  };

  // 회원 프로필 설정 함수
  const setMemberProfile = (profile: MemberProfile) => {
    dispatch({ type: 'SET_MEMBER_PROFILE', payload: profile }); // 상태 업데이트
  };

  // 컨텍스트 제공
  return (
    <AuthContext.Provider value={{ state, login, logout, setMemberProfile }}>
      {children}
    </AuthContext.Provider>
  );
}; 