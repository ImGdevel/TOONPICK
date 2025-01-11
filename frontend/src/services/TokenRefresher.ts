import { api } from '@api';
import Logger from '@utils/Logger';
import TokenManager from '@services/TokenManager';
import { AxiosRequestConfig, AxiosResponse } from 'axios';

// 실패한 요청 관리 큐
let failedQueue: Array<{
  requestConfig: AxiosRequestConfig;
  resolve: (value: AxiosResponse<any> | PromiseLike<AxiosResponse<any>>) => void;
  reject: (reason?: any) => void;
}> = [];
let isRefreshing = false;

// 큐 처리 함수
const processQueue = (error: any, token: string | null) => {
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

// 토큰 갱신 핸들러
const TokenRefresher = {
  handleTokenExpiration: async (originalRequest: AxiosRequestConfig) => {
    if (!isRefreshing) {
      isRefreshing = true;
      try {
        const newAccessToken = await TokenRefresher.refreshAccessToken();
        processQueue(null, newAccessToken);
        isRefreshing = false;
        return api(originalRequest);
      } catch (refreshError) {
        Logger.error('Token Refresh Error:', refreshError as Record<string, any>);
        processQueue(refreshError, null);
        isRefreshing = false;
        TokenManager.clearAccessToken();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return new Promise((resolve, reject) => {
      failedQueue.push({
        requestConfig: originalRequest,
        resolve,
        reject,
      });
    });
  },

  refreshAccessToken: async (): Promise<string> => {
    try {
      const response = await api.post('/api/reissue', {}, { withCredentials: true });
      const newAccessToken = TokenManager.extractAccessTokenFromHeader(response.headers);
      if (newAccessToken) {
        TokenManager.setAccessToken(newAccessToken);
        return newAccessToken;
      } else {
        throw new Error('Failed to reissue access token');
      }
    } catch (error) {
      Logger.error('Token Refresh Failed:', error as Record<string, any>);
      TokenManager.clearAccessToken();
      throw error;
    }
  },
};

export default TokenRefresher;
