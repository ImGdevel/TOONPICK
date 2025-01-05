import AuthToken from './AuthToken';
import api from './ApiService';

interface LoginResponse {
  success: boolean;
  message?: string;
}

interface JoinFormat {
  username: string;
  email: string;
  password: string;
}

type Provider = 'google' | 'kakao' | 'naver';

class AuthService {
  private static instance: AuthService;

  private constructor() {}

  public static getInstance(): AuthService {
    if (!AuthService.instance) {
      AuthService.instance = new AuthService();
    }
    return AuthService.instance;
  }

  // 로그인
  public async login(
    username: string,
    password: string,
    rememberMe: boolean = false,
    loginCallback?: () => void
  ): Promise<LoginResponse> {
    try {
      const response = await api.post<{ accessToken: string }>(
        '/login',
        { username, password },
        { authRequired: false }
      );

      const { accessToken } = response;
      AuthToken.setAccessToken(accessToken, rememberMe);
      loginCallback?.();
      return { success: true };
    } catch (error) {
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 회원가입
  public async signup(
    username: string,
    password: string,
    confirmPassword: string
  ): Promise<LoginResponse> {
    if (password !== confirmPassword) {
      return { success: false, message: 'Passwords do not match.' };
    }

    try {
      const joinFormat: JoinFormat = {
        username,
        email: username,
        password,
      };

      await api.post('/join', joinFormat, { authRequired: false });
      return { success: true };
    } catch (error) {
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 로그아웃
  public async logout(): Promise<LoginResponse> {
    try {
      await api.post('/logout', null, { authRequired: true });
      AuthToken.clearAccessToken();
      return { success: true };
    } catch (error) {
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 로그인 상태 확인
  public isLoggedIn(): boolean {
    const isAuthenticated = AuthToken.isAuthenticated();
    console.log("Login status:", isAuthenticated);
    return isAuthenticated;
  }

  // 소셜 로그인
  public socialLogin(provider: Provider): void {
    const loginUrl = `${process.env.REACT_APP_API_URL || 'http://localhost:8080'}/oauth2/authorization/${provider}`;
    window.location.href = loginUrl;
  }

  // 소셜 로그인 콜백 처리
  public async handleSocialLoginCallback(
    loginCallback?: () => void
  ): Promise<LoginResponse> {
    try {
      // Refresh Access Token from server (HttpOnly cookie used for Refresh Token)
      const newAccessToken = await AuthToken.refreshAccessToken();
      
      if (newAccessToken) {
        loginCallback?.();
        return { success: true };
      }
      return { success: false, message: 'No access token found during callback.' };
    } catch (error) {
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }
}

export default AuthService.getInstance();
