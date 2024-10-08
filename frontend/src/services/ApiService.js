import axios from 'axios';
import { AuthToken } from './AuthToken';

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
    const accessToken = AuthToken.getAccessToken();
    if (config.authRequired && accessToken && accessToken.trim() !== '') {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response.status === 401) {
      const errorMessage = error.response.data.error;

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
const handleTokenExpiration = async (originalRequest) => {
  try {
    const newAccessToken = await AuthToken.refreshAccessToken();

    // 원래 요청에 새로운 Access 토큰을 추가
    originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
    return api(originalRequest);
  } catch (reissueError) {
    return Promise.reject(reissueError);
  }
};

export default api;
