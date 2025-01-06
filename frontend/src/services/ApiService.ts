import axios, { AxiosHeaders, AxiosRequestConfig, AxiosResponse } from 'axios';
import AuthToken from '@services/AuthToken';

// 실패한 요청 큐 관리
let isRefreshing = false;
let failedQueue: Array<{
  requestConfig: AxiosRequestConfig;
  resolve: (value: AxiosResponse<any> | PromiseLike<AxiosResponse<any>>) => void;
  reject: (reason?: any) => void;
}> = [];

// 실패한 요청 큐 처리 함수
const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(({ requestConfig, resolve, reject }) => {
    if (token) {
      requestConfig.headers = {
        ...requestConfig.headers,
        Authorization: `Bearer ${token}`,
      };
      resolve(api(requestConfig));
    } else {
      reject(error);
    }
  });
  failedQueue = [];
};

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
        if (!isRefreshing) {
          isRefreshing = true;

          try {
            const newAccessToken = await AuthToken.refreshAccessToken();
            AuthToken.setAccessToken(newAccessToken);

            processQueue(null, newAccessToken);
            isRefreshing = false;

            return api(originalRequest); // 요청 재시도
          } catch (refreshError) {
            processQueue(refreshError, null);
            isRefreshing = false;
            AuthToken.clearAccessToken();
            window.location.href = '/login';
            return Promise.reject(refreshError);
          }
        }

        // 큐에 요청 추가
        return new Promise((resolve, reject) => {
          failedQueue.push({
            requestConfig: originalRequest, // 요청 정보 저장
            resolve,
            reject,
          });
        });
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

export default api;
