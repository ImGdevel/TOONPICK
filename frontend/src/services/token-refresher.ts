import { api } from '@api';
import TokenManager from '@/services/token-manager';
import { AxiosRequestConfig, AxiosResponse } from 'axios';

// 실패한 요청 관리 큐
let failedQueue: Array<{
  requestConfig: AxiosRequestConfig;
  resolve: (value: AxiosResponse<any> | PromiseLike<AxiosResponse<any>>) => void;
  reject: (reason?: any) => void;
}> = [];
let isRefreshing = false;

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

const TokenRefresher = {

  /**
   * @param originalRequest 
   * @returns 
   */
  handleTokenExpiration: async (originalRequest: AxiosRequestConfig) => {
    if (!isRefreshing) {
      isRefreshing = true;
      try {
        const newAccessToken = await TokenRefresher.refreshAccessToken();
        processQueue(null, newAccessToken);
        return api(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError, null);
        TokenManager.clearAccessToken();
        TokenManager.clearRefreshToken();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return new Promise((resolve, reject) => {
      failedQueue.push({ requestConfig: originalRequest, resolve, reject });
    });
  },

  /**
   * 토큰 재발급
   * @returns 
   */
  refreshAccessToken: async (): Promise<string> => {
    try {
      const response = await api.post('/reissue', {}, { withCredentials: true });
      const newAccessToken = TokenManager.extractAccessTokenFromHeader(response.headers);

      if (newAccessToken) {
        TokenManager.setAccessToken(newAccessToken);
        return newAccessToken;
      } else {
        throw new Error('Failed to reissue access token');
      }
    } catch (error) {
      TokenManager.clearAccessToken();
      TokenManager.clearRefreshToken();
      throw error;
    }
  },
};

export default TokenRefresher;
