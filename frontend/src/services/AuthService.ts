import TokenManager from '@services/TokenManager';
import api from '@services/ApiService';
import TokenRefresher from '@services/TokenRefresher';


export const AuthService = {
  login: async (username: string, password: string, loginCallback?: () => void): Promise<{ success: boolean; message?: string }> => {
    try {
      const response = await api.post<{ accessToken: string }>(
        '/login',
        { username, password },
      );
      const accessToken = TokenManager.extractAccessTokenFromHeader(response.headers);
      if (accessToken) {
        TokenManager.setAccessToken(accessToken);
        loginCallback?.();
        return { success: true };
      } else {
        return { success: false, message: 'Login failed' };
      }
    } catch (error: any) {
      return { success: false, message: error.message };
    }
  },

  signup: async (username: string, password: string, confirmPassword: string): Promise<{ success: boolean; message?: string }> => {
    if (password !== confirmPassword) {
      return { success: false, message: 'Passwords do not match.' };
    }
    try {
      const joinFormat = {
        username,
        email: username,
        password,
      };
      await api.post('/join', joinFormat);
      return { success: true };
    } catch (error: any) {
      return { success: false, message: error.message };
    }
  },

  logout: async (): Promise<{ success: boolean; message?: string }> => {
    try {
      await api.post('/logout', {});
      TokenManager.clearAccessToken();
      return { success: true };
    } catch (error: any) {
      return { success: false, message: error.message };
    }
  },

  isLoggedIn: (): boolean => {
    const accessToken = TokenManager.getAccessToken();
    if (!accessToken) return false;
    return !TokenManager.isAccessTokenExpired(accessToken);
  },

  socialLogin: (provider: string): void => {
    const loginUrl = `http://localhost:8080/oauth2/authorization/${provider}`;
    window.location.href = loginUrl;
  },

  handleSocialLoginCallback: async (loginCallback?: () => void): Promise<{ success: boolean; message?: string }> => {
    try {
      const accessToken = await TokenRefresher.refreshAccessToken();
      if (accessToken) {
        TokenManager.setAccessToken(accessToken);
        loginCallback?.();
        return { success: true };
      } else {
        return { success: false, message: 'No access token found in URL' };
      }
    } catch (error: any) {
      return { success: false, message: error.message };
    }
  },
};

export default AuthService;
