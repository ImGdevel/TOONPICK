import axios, { AxiosError, AxiosHeaders, AxiosResponse } from 'axios';
import { handleErrorResponse } from './ErrorHandler';
import TokenManager from '@/services/TokenManager';

// Axios 인스턴스 생성
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 요청 제한 시간 추가
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
    const updatedConfig = await handleErrorResponse(error);
    if (updatedConfig) {
      // Retry the failed request with updated config
      return api.request(updatedConfig);
    }
    return Promise.reject(error);
  }
);

export default api;
