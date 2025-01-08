import { AxiosRequestConfig, AxiosResponse } from 'axios';
import api from '@services/ApiService';

export let failedQueue: Array<{
  requestConfig: AxiosRequestConfig;
  resolve: (value: AxiosResponse<any> | PromiseLike<AxiosResponse<any>>) => void;
  reject: (reason?: any) => void;
}> = [];

// 실패한 요청 큐 처리
export const processQueue = (error: any, token: string | null = null) => {
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