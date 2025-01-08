import TokenRefresher from '@services/TokenRefresher';
import { failedQueue, processQueue } from '@services/RequestQueue';
import api from '@services/ApiService';
import { AxiosRequestConfig } from 'axios';
import TokenManager from '@services/TokenManager';

// 액세스 토큰 갱신 상태 추적 변수
let isRefreshing = false;

export const TokenExpirationHandler = async (originalRequest: AxiosRequestConfig) => {
  if (!isRefreshing) {
    isRefreshing = true;

    try {
      const newAccessToken = await TokenRefresher.refreshAccessToken();
      processQueue(null, newAccessToken);
      isRefreshing = false;

      return api(originalRequest);
    } catch (refreshError) {
      processQueue(refreshError, null);
      isRefreshing = false;
      TokenManager.clearAccessToken();
      window.location.href = '/login';
      return Promise.reject(refreshError);
    }
  }

  // 큐에 요청 추가
  return new Promise((resolve, reject) => {
    failedQueue.push({
      requestConfig: originalRequest,
      resolve,
      reject,
    });
  });
};

export default TokenExpirationHandler; 