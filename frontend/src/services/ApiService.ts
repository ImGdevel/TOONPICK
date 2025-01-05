import axios, { 
  AxiosInstance, 
  AxiosResponse, 
  AxiosError, 
  InternalAxiosRequestConfig,
  AxiosRequestHeaders 
} from 'axios';
import AuthToken from './AuthToken';

interface CustomAxiosRequestConfig extends Omit<InternalAxiosRequestConfig, 'headers'> {
  authRequired?: boolean;
  _retry?: boolean;
  headers?: AxiosRequestHeaders;
}

class ApiService {
  private static instance: ApiService;
  private api: AxiosInstance;

  private constructor() {
    this.api = axios.create({
      baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
    });

    this.setupInterceptors();
  }

  
  public static getInstance(): ApiService {
    if (!ApiService.instance) {
      ApiService.instance = new ApiService();
    }
    return ApiService.instance;
  }

  private setupInterceptors(): void {
    // 요청 인터셉터
    this.api.interceptors.request.use(
      (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        const accessToken = AuthToken.getAccessToken();
        if ((config as CustomAxiosRequestConfig).authRequired && accessToken) {
          if (!config.headers) {
            config.headers = new axios.AxiosHeaders();
          }
          config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        return config;
      },
      (error: AxiosError): Promise<AxiosError> => {
        return Promise.reject(error);
      }
    );

    // 응답 인터셉터
    this.api.interceptors.response.use(
      (response: AxiosResponse): AxiosResponse => response,
      async (error: AxiosError): Promise<any> => {
        const originalRequest = error.config as CustomAxiosRequestConfig;
        if (error.response?.status === 401) {
          const errorMessage = (error.response.data as { error: string }).error;

          if (errorMessage === 'access token expired') {
            if (!originalRequest._retry) {
              originalRequest._retry = true;
              return this.handleTokenExpiration(originalRequest);
            }
          } else if (errorMessage === 'Refresh token expired') {
            AuthToken.clearAccessToken();
            window.location.href = '/login';
            return Promise.reject(new Error('리프레시 토큰이 만료되었습니다. 다시 로그인하세요.'));
          } else if (errorMessage === 'invalid access token') {
            AuthToken.clearAccessToken();
            window.location.href = '/login';
            return Promise.reject(new Error('잘못된 토큰입니다.'));
          }
        }
        return Promise.reject(error);
      }
    );
  }

  private async handleTokenExpiration(originalRequest: CustomAxiosRequestConfig): Promise<AxiosResponse> {
    try {
      const newAccessToken = await AuthToken.refreshAccessToken();
      
      if (!originalRequest.headers) {
        originalRequest.headers = new axios.AxiosHeaders();
      }
      originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
      return this.api(originalRequest);
    } catch (reissueError) {
      return Promise.reject(reissueError);
    }
  }

  // API 메서드들을 위한 public 인터페이스
  public async get<T>(url: string, config?: CustomAxiosRequestConfig): Promise<T> {
    const response = await this.api.get<T>(url, config);
    return response.data;
  }

  public async post<T>(url: string, data?: any, config?: CustomAxiosRequestConfig): Promise<T> {
    const response = await this.api.post<T>(url, data, config);
    return response.data;
  }

  public async put<T>(url: string, data?: any, config?: CustomAxiosRequestConfig): Promise<T> {
    const response = await this.api.put<T>(url, data, config);
    return response.data;
  }

  public async delete<T>(url: string, config?: CustomAxiosRequestConfig): Promise<T> {
    const response = await this.api.delete<T>(url, config);
    return response.data;
  }
}

export default ApiService.getInstance();
