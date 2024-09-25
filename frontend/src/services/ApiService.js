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
    console.log("put in access token!")
    if (config.authRequired && accessToken) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    console.log("fail reject")
    return Promise.reject(error);
  }
);

// 응답 인터셉터
api.interceptors.response.use(
  (response) => {
    console.log("response")
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    console.log("response fail!")
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        console.log("request?")
        const response = await api.post('/reissue', { withCredentials: true });
        const newAccessToken = response.data.accessToken;

        // 새 액세스 토큰 저장
        localStorage.setItem('accessToken', newAccessToken);

        // 원래 요청에 새로운 토큰 설정
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        
        // 원래 요청 재전송
        return api(originalRequest);
      } catch (reissueError) {
        console.error('토큰 재발급 실패:', reissueError);
        AuthService.logout();
        return Promise.reject(reissueError);
      }
    }
    return Promise.reject(error);
  }
);

export default api;
