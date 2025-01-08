import axios, { AxiosHeaders, AxiosRequestConfig, AxiosResponse } from 'axios';
import TokenManager from '@/services/TokenManager';
import TokenExpirationHandler from '@/services/TokenExpirationHandler';

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
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401) {
      const errorMessage: string = error.response.data.error;

      if (errorMessage === 'access token expired') {
        return TokenExpirationHandler(originalRequest);
      } else if (errorMessage === 'Refresh token expired') {
        TokenManager.clearAccessToken();
        window.location.href = '/login';
        return Promise.reject(new Error('리프레시 토큰이 만료되었습니다. 다시 로그인하세요.'));
      } else if (errorMessage === 'invalid access token') {
        TokenManager.clearAccessToken();
        window.location.href = '/login';
        return Promise.reject(new Error('잘못된 토큰입니다.'));
      }
    }

    return Promise.reject(error);
  }
);

export default api;
