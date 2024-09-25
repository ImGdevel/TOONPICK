// src/services/ApiService.js

import axios from 'axios';
import { AuthService } from './AuthService';

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
    const accessToken = AuthService.getAccessToken();
    if (config.authRequired && accessToken && accessToken.trim() !== '') {
      console.log("put in access token!", accessToken);
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    } else if (config.authRequired) {
      console.warn("AccessToken is not available.");
      // AccessToken이 없으면 별도의 처리 (예: 로그인 페이지로 리다이렉트)
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);



// Axios 응답 인터셉터 설정
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 401 에러 처리
    if (error.response.status === 401) {
      const errorMessage = error.response.data.error;

      if (errorMessage === 'access token expired') {
        // 액세스 토큰 만료 시, 토큰 재발급 처리
        if (!originalRequest._retry) {
          originalRequest._retry = true;
          return handleTokenExpiration(originalRequest);
        }
      } else if (errorMessage === 'invalid access token') {
        // 잘못된 토큰일 경우 로그아웃 및 리다이렉션 처리
        AuthService.logout();
        window.location.href = '/login';
        return Promise.reject(new Error('잘못된 토큰입니다.'));
      }
    }

    return Promise.reject(error);
  }
);


// 액세스 토큰 만료 처리 메서드
const handleTokenExpiration = async (originalRequest) => {
  try {
    const response = await api.post('/api/reissue', { withCredentials: true });
    const newAccessToken = response.headers['authorization'] 
      ? response.headers['authorization'].split(' ')[1] 
      : null;

    if (newAccessToken) {
      // 새 액세스 토큰 저장
      localStorage.setItem('accessToken', newAccessToken);

      // 원래 요청에 새로운 토큰 설정
      originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

      // 원래 요청 재전송
      return api(originalRequest);
    } else {
      throw new Error('토큰 재발급 실패');
    }
  } catch (reissueError) {
    console.error('토큰 재발급 실패:', reissueError);
    AuthService.logout();
    window.location.href = '/login';
    return Promise.reject(reissueError);
  }
};

export default api;
