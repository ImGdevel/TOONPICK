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
        { username, password, rememberMe }, 
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
        password
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
    const accessToken = AuthToken.getAccessToken();
    if (!accessToken) {
      console.log("AccessToken is not available.");
      return false;
    }

    const isValid = !AuthToken.isAccessTokenExpired(accessToken);
    console.log("Login status:", isValid);
    return isValid;
  }

  // 소셜 로그인
  public socialLogin(provider: Provider): void {
    const loginUrl = `${process.env.REACT_APP_API_URL}/oauth2/authorization/${provider}`;
    window.location.href = loginUrl;
  }

  // 소셜 로그인 콜백 처리
  public async handleSocialLoginCallback(
    loginCallback?: () => void
  ): Promise<LoginResponse> {
    try {
      const accessToken = await AuthToken.refreshAccessToken();
      
      if (accessToken) {
        localStorage.setItem('accessToken', accessToken);
        loginCallback?.();
        return { success: true };
      }
      return { success: false, message: 'No access token found in URL' };
    } catch (error) {
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }
}

export default AuthService.getInstance();
