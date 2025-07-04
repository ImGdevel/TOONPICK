import { api, Response } from '@api';
import { Routes } from '@constants/routes'
import TokenManager from '@services/token-manager';
import TokenRefresher from '@services/token-refresher';
import Logger from '@utils/logger';
import Cookies from 'js-cookie';

export const AuthService = {
  // 로그인
  login: async (username: string, password: string, loginCallback?: () => void): Promise<Response> => {
    try {
      const response = await api.post<{ accessToken: string }>('/login', { username, password });
      const accessToken = TokenManager.extractAccessTokenFromHeader(response.headers);

      if (accessToken) {
        TokenManager.setAccessToken(accessToken);
        loginCallback?.();
        return { success: true };
      } else {
        Logger.error('Login failed: No access token found in response headers');
        return { success: false, message: 'Login failed: Unable to retrieve access token' };
      }
    } catch (error: any) {
      Logger.error('Login error:', error);
      return { success: false, message: error.response?.data || 'Login failed due to an unknown error' };
    }
  },

  // 회원가입
  signup: async (username: string, email: string ,password: string): Promise<Response> => {
    try {
      const joinPayload = {
        username,
        email: username,
        password,
      };

      await api.post('/join', joinPayload);
      return { success: true };
    } catch (error: any) {
      Logger.error('Signup error:', error);
      return { success: false, message: error.response?.data || 'Signup failed due to an unknown error' };
    }
  },

  // 로그아웃
  logout: async (logoutCallback?: () => void): Promise<Response> => {
    try {
      TokenManager.clearAccessToken();
      await api.post('/logout');
      logoutCallback?.();
      return { success: true };
    } catch (error: any) {
      Logger.error('Logout error:', error);
      return { success: false, message: error.response?.data || 'Logout failed due to an unknown error' };
    }
  },

  // 로그인 여부 확인
  isLoggedIn: (): boolean => {
    const accessToken = TokenManager.getAccessToken();
    return !!accessToken && !TokenManager.isAccessTokenExpired(accessToken);
  },

  // 소셜 로그인
  socialLogin: (provider: string): void => {
      const redirectUri = window.location.origin + Routes.LOGIN_CALLBACK;
  
      // redirect_uri를 쿠키에 저장 (유효기간: 5분)
      Cookies.set('redirect_uri', redirectUri, { path: '/', expires: 1 / 288 });
  
      // OAuth2 로그인 요청 (redirect_uri는 URL에서 제거)
      const loginUrl = `${process.env.REACT_APP_API_URL || 'http://localhost:8080'}/oauth2/authorization/${provider}`;
      window.location.href = loginUrl;
  },
  


  // 소셜 로그인 콜백 처리
  handleSocialLoginCallback: async (loginCallback?: () => void): Promise<Response> => {
    try {
      const accessToken = await TokenRefresher.refreshAccessToken();
      if (accessToken) {
        TokenManager.setAccessToken(accessToken);
        loginCallback?.();
        return { success: true };
      } else {
        Logger.error('Social login callback error: No access token found in response');
        return { success: false, message: 'Social login failed: No access token found' };
      }
    } catch (error: any) {
      Logger.error('Social login callback error:', error);
      return { success: false, message: error.response?.data || 'Social login callback failed due to an unknown error' };
    }
  },
};

export default AuthService;
