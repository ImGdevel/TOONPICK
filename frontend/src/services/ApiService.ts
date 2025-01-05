import axios, { AxiosResponse, AxiosHeaders, InternalAxiosRequestConfig } from 'axios';
import { AuthToken } from './AuthToken';

// InternalAxiosRequestConfig에 사용자 정의 속성 추가
interface CustomAxiosRequestConfig extends InternalAxiosRequestConfig {
  authRequired?: boolean; // 추가된 속성
  _retry?: boolean; // Add the _retry property
}

// Axios 인스턴스 생성
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true,
});

// 요청 인터셉터
api.interceptors.request.use(
  (config: CustomAxiosRequestConfig) => {
    const accessToken = AuthToken.getAccessToken();
    if (config.authRequired && accessToken && accessToken.trim() !== '') {
      // headers가 undefined인 경우 AxiosHeaders.from으로 초기화
      if (!config.headers) {
        config.headers = AxiosHeaders.from({});
      }

      config.headers.set('Authorization', `Bearer ${accessToken}`);
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
api.interceptors.response.use(
  (response: AxiosResponse) => response,
  async (error) => {
    const originalRequest = error.config as CustomAxiosRequestConfig;
    if (error.response?.status === 401) {
      const errorMessage: string = error.response.data.error;

      if (errorMessage === 'access token expired') {
        if (!originalRequest._retry) {
          originalRequest._retry = true;
          return handleTokenExpiration(originalRequest);
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

// Access 토큰 만료 처리
const handleTokenExpiration = async (originalRequest: CustomAxiosRequestConfig): Promise<any> => {
  try {
    const newAccessToken = await AuthToken.refreshAccessToken();
    if (!originalRequest.headers) {
      originalRequest.headers = AxiosHeaders.from({});
    }
    originalRequest.headers.set('Authorization', `Bearer ${newAccessToken}`);
    return api(originalRequest);
  } catch (reissueError) {
    return Promise.reject(reissueError);
  }
};

export default api;
