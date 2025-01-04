import api from "./ApiService";

// 상수 정의
const ACCESS_TOKEN_KEY = "accessToken";
const TOKEN_TYPE = "Bearer";

// 타입 정의
interface TokenPayload {
  exp: number;
  sub: string;  // 사용자 식별자
  roles: string[];  // 사용자 권한
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
      throw new Error('토큰이 유효하지 않습니다.');
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

  // Access 토큰 인증 여부 확인
  public isAuthenticated(): boolean {
    const token = this.getAccessToken();
    return token !== null && !this.isTokenExpired(token);
  }

  // Access 토큰 사용자 권한 조회
  public getUserRoles(): string[] {
    const payload = this.getTokenPayload();
    return payload?.roles || [];
  }

  // Access 토큰 사용자 식별자 조회
  public getUserId(): string | null {
    const payload = this.getTokenPayload();
    return payload?.sub || null;
  }

  // Access 토큰 만료 여부 확인
  private isTokenExpired(token: string): boolean {
    try {
      const payload = this.getTokenPayload();
      if (!payload) return true;
      
      // 만료 시간 5분 전부터는 토큰을 만료된 것으로 간주
      const expirationBuffer = 5 * 60 * 1000; 
      return (payload.exp * 1000) - expirationBuffer < Date.now();
    } catch (error) {
      console.error("토큰 검증 실패:", error);
      return true;
    }
  }

  // Access 토큰 헤더에서 추출
  public extractAccessTokenFromHeader(headers: Record<string, string>): string | null {
    const authHeader = headers["Authorization"];
    if (!authHeader?.startsWith(TOKEN_TYPE)) return null;
    
    return authHeader.slice(TOKEN_TYPE.length).trim();
  }

  // Access 토큰 재발급
  public async refreshAccessToken(): Promise<string> {
    try {
      const response = await api.post<{ headers: Record<string, string> }>('/api/reissue', {
        withCredentials: true
      });

      const newAccessToken = this.extractAccessTokenFromHeader(response.headers);

      if (!newAccessToken) {
        throw new Error('토큰 재발급 실패: 응답에 토큰이 없습니다.');
      }

      this.setAccessToken(newAccessToken);
      return newAccessToken;

    } catch (error) {
      this.clearAccessToken();
      this.redirectToLogin();
      throw new Error('토큰 재발급 실패: ' + (error instanceof Error ? error.message : '알 수 없는 오류'));
    }
  }

  // 로그인 페이지로 리디렉션
  private redirectToLogin(): void {
    window.location.href = '/login';
  }

  // Access 토큰 만료 여부 확인
  public isAccessTokenExpired(token: string): boolean {
    try {
      const payload = this.getTokenPayload();
      if (!payload) return true;
      
      const expirationBuffer = 5 * 60 * 1000;
      return (payload.exp * 1000) - expirationBuffer < Date.now();
    } catch (error) {
      console.error("토큰 검증 실패:", error);
      return true;
    }
  }
}

export default AuthTokenService.getInstance(); 