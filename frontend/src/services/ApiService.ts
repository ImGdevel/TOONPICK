import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { AuthTokens } from '../types/auth';
import { getStoredTokens, setStoredTokens, removeStoredTokens } from './AuthToken';


class ApiService {
  private static instance: ApiService;
  private api: AxiosInstance;

  // 생성자 설정
  private constructor() {
    this.api = axios.create({
      baseURL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080',
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
    });

    this.setupInterceptors();
  }

  // 싱글톤 패턴 적용
  public static getInstance(): ApiService {
    if (!ApiService.instance) {
      ApiService.instance = new ApiService();
    }
    return ApiService.instance;
  }

  // 인터셉터 설정
  private setupInterceptors(): void {
    this.api.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const tokens = getStoredTokens();
        if (tokens?.accessToken && config.headers) {
          config.headers['Authorization'] = `Bearer ${tokens.accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    

    // 응답 인터셉터 설정
    this.api.interceptors.response.use(
      (response: AxiosResponse) => response,
      async (error) => {
        const originalRequest = error.config;
        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;
          try {
            const tokens = getStoredTokens();
            if (!tokens?.refreshToken) throw new Error('No refresh token');

            const response = await this.refreshToken(tokens.refreshToken);
            setStoredTokens(response.data.tokens);
            
            originalRequest.headers['Authorization'] = 
              `Bearer ${response.data.tokens.accessToken}`;
            return this.api(originalRequest);
          } catch (refreshError) {
            removeStoredTokens();
            throw refreshError;
          }
        }
        return Promise.reject(error);
      }
    );
  }

  // 토큰 갱신 요청
  private async refreshToken(refreshToken: string): Promise<AxiosResponse<{ tokens: AuthTokens }>> {
    return await this.api.post('/auth/refresh', { refreshToken });
  }

  // Axios 인스턴스 반환
  public getAxiosInstance(): AxiosInstance {
    return this.api;
  }
}

export default ApiService.getInstance(); 