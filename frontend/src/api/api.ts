import axios, { AxiosError, AxiosHeaders, AxiosResponse } from 'axios';
import TokenManager from '@/services/token-manager';
import TokenRefresher from '@/services/token-refresher';
import Logger from '@/utils/logger';

// Axios 인스턴스 생성
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
  withCredentials: true,
});

// 요청 인터셉터
api.interceptors.request.use(
  (config) => {
    const accessToken = TokenManager.getAccessToken();
    if (!config.headers) {
      config.headers = AxiosHeaders.from({});
    }
    if (accessToken && accessToken.trim() !== '') {
      (config.headers as AxiosHeaders).set('Authorization', `Bearer ${accessToken}`);
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
api.interceptors.response.use(
  (response: AxiosResponse) => response,
  async (error: AxiosError) => {

    const errorStatus = error.response?.status;
    const errorMessage = error.response?.data;

    Logger.error('API Error:', { status: errorStatus, message: errorMessage });

    if (errorStatus === 401) {
      const originalRequest = error.config!;
      if (errorMessage === 'Authentication credentials are required.') {
        try {
          return await TokenRefresher.handleTokenExpiration(originalRequest);
        } catch (refreshError) {
          return Promise.reject(refreshError);
        }
      } else if (errorMessage === 'Invalid refresh token') {
        TokenManager.clearAccessToken();
        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  }
);

export default api;