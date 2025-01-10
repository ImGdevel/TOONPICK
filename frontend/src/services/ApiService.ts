import axios, { AxiosHeaders, AxiosResponse } from 'axios';
import TokenManager from '@/services/TokenManager';

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

    if(error.response){
      const status = error.response.status;
      const message = error.response.data || 'Unknown error';

      console.log(error.response, "message : ", message);

      if (status === 401) {
        console.error('잘못된 요청:', message);
      } else if (status >= 500) {
        console.error('서버 오류:', message);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
