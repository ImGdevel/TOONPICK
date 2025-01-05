import api from "./ApiService";

// 상수 정의
const ACCESS_TOKEN_KEY = "accessToken";

// 타입 정의
interface TokenPayload {
  exp: number;
  sub: string; 
  roles: string[]; 
  [key: string]: any;
}

class AuthTokenService {
  private static instance: AuthTokenService;

  private constructor() {}

  // 싱글톤 패턴
  public static getInstance(): AuthTokenService {
    if (!AuthTokenService.instance) {
      AuthTokenService.instance = new AuthTokenService();
    }
    return AuthTokenService.instance;
  }

  // 토큰 설정
  public setAccessToken(token: string, rememberMe: boolean = false): void {
    if (!token) {
      throw new Error("유효하지 않은 토큰입니다.");
    }

    if (rememberMe) {
      localStorage.setItem(ACCESS_TOKEN_KEY, token);
    } else {
      sessionStorage.setItem(ACCESS_TOKEN_KEY, token);
    }
  }

  // Access 토큰 조회
  public getAccessToken(): string | null {
    return localStorage.getItem(ACCESS_TOKEN_KEY) || sessionStorage.getItem(ACCESS_TOKEN_KEY);
  }

  // Access 토큰 삭제
  public clearAccessToken(): void {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    sessionStorage.removeItem(ACCESS_TOKEN_KEY);
  }

  // Access 토큰 페이로드 조회
  public getTokenPayload(): TokenPayload | null {
    const token = this.getAccessToken();
    if (!token) return null;

    try {
      return JSON.parse(atob(token.split(".")[1])) as TokenPayload;
    } catch (error) {
      console.error("토큰 디코딩 실패:", error);
      return null;
    }
  }

  // Access 토큰 만료 여부 확인
  public isAccessTokenExpired(): boolean {
    const payload = this.getTokenPayload();
    if (!payload) return true;

    const expirationBuffer = 5 * 60 * 1000; // 5분 전부터 만료 처리
    return payload.exp * 1000 - expirationBuffer < Date.now();
  }

  // 로그인 상태 확인 (새로 추가된 메서드)
  public isAuthenticated(): boolean {
    const accessToken = this.getAccessToken();
    if (!accessToken) return false;

    return !this.isAccessTokenExpired(); // 토큰이 존재하고 만료되지 않았는지 확인
  }

  // Access 토큰 재발급 (Refresh Token은 HttpOnly 쿠키에 저장됨)
  public async refreshAccessToken(): Promise<string> {
    try {
      const response = await api.post<{ accessToken: string }>("/api/reissue", null, {
        withCredentials: true,
        authRequired: false, // 재발급 시 인증이 필요하지 않음
      });

      const newAccessToken = response.accessToken;
      if (!newAccessToken) {
        throw new Error("토큰 재발급 실패: 응답에 토큰이 없습니다.");
      }

      this.setAccessToken(newAccessToken);
      return newAccessToken;
    } catch (error) {
      console.error("Access 토큰 재발급 실패:", error);
      this.clearAccessToken();
      this.redirectToLogin();
      throw error;
    }
  }

  // 로그인 페이지로 리디렉션
  private redirectToLogin(): void {
    window.location.href = "/login";
  }
}

export default AuthTokenService.getInstance();
