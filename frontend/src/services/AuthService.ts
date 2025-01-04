import { AxiosResponse } from 'axios';
import ApiService from './ApiService';
import { setStoredTokens, removeStoredTokens, getStoredTokens } from './AuthToken';
import { 
  LoginCredentials, 
  RegisterCredentials, 
  AuthResponse,
  UserResponse 
} from '../types/auth';

class AuthService {
  private api = ApiService.getAxiosInstance();

  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    try {
      const response: AxiosResponse<AuthResponse> = await this.api.post('/auth/login', credentials);
      if (response.data.success) {
        setStoredTokens(response.data.data.tokens);
      }
      return response.data;
    } catch (error) {
      throw new Error('Login failed');
    }
  }

  async register(credentials: RegisterCredentials): Promise<AuthResponse> {
    try {
      const response: AxiosResponse<AuthResponse> = await this.api.post('/auth/register', credentials);
      if (response.data.success) {
        setStoredTokens(response.data.data.tokens);
      }
      return response.data;
    } catch (error) {
      throw new Error('Registration failed');
    }
  }

  async logout(): Promise<void> {
    try {
      await this.api.post('/auth/logout');
      removeStoredTokens();
    } catch (error) {
      throw new Error('Logout failed');
    }
  }

  async getCurrentUser(): Promise<UserResponse> {
    try {
      const response: AxiosResponse<UserResponse> = await this.api.get('/auth/me');
      return response.data;
    } catch (error) {
      throw new Error('Failed to get current user');
    }
  }

  async refreshToken(refreshToken: string): Promise<AuthResponse> {
    try {
      const response = await this.api.post('/auth/refresh', { refreshToken });
      if (response.data.success) {
        setStoredTokens(response.data.data.tokens);
      }
      return response.data;
    } catch (error) {
      throw new Error('Token refresh failed');
    }
  }

  async socialLogin(provider: string, code: string): Promise<AuthResponse> {
    try {
      const response = await this.api.post('/auth/social-login', { provider, code });
      if (response.data.success) {
        setStoredTokens(response.data.data.tokens);
      }
      return response.data;
    } catch (error) {
      throw new Error('Social login failed');
    }
  }

  isLoggedIn(): boolean {
    const tokens = getStoredTokens();
    return !!tokens?.accessToken;
  }
}

export default new AuthService(); 