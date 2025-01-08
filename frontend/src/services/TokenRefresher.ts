import api from '@services/ApiService';
import TokenManager from '@services/TokenManager';

// 토큰 갱신 서비스
export const TokenRefresher = {
  refreshAccessToken: async (): Promise<string> => {
    try {
      const response = await api.post('/api/reissue', {}, { withCredentials: true });
      const newAccessToken = TokenManager.extractAccessTokenFromHeader(response.headers);
      if (newAccessToken) {
        TokenManager.setAccessToken(newAccessToken);
        return newAccessToken;
      } else {
        throw new Error('토큰 재발급 실패');
      }
    } catch (reissueError) {
      console.error('토큰 재발급 실패:', reissueError);
      TokenManager.clearAccessToken();
      window.location.href = '/login';
      throw reissueError;
    }
  },
};

export default TokenRefresher; 