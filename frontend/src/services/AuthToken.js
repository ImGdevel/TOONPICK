// src/services/AuthToken.js
import api from "./ApiService";

const ACCESS_TOKEN_KEY = "accessToken";

// Access Token 관련 함수
export const AuthToken = {
  setAccessToken: (token) => {
    localStorage.setItem(ACCESS_TOKEN_KEY, token);
  },

  getAccessToken: () => {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
  },

  clearAccessToken: () => {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
  },

  isAccessTokenExpired: (token) => {
    try {
      const payload = JSON.parse(atob(token.split(".")[1]));
      return payload.exp * 1000 < Date.now();
    } catch (error) {
      console.error("Invalid token format or decoding error:", error);
      return true;
    }
  },
  

  // 헤더에서 Access 토큰 추출
  extractAccessTokenFromHeader: (headers) => {
    return headers["authorization"]
      ? headers["authorization"].split(" ")[1]
      : null;
  },

  // 토큰 재발급
  refreshAccessToken: async () => {
    try {
      const response = await api.post('/api/reissue', { withCredentials: true });
      const newAccessToken = AuthToken.extractAccessTokenFromHeader(response.headers);

      if (newAccessToken) {
        console.log("success reissue access token")
        AuthToken.setAccessToken(newAccessToken);
        return newAccessToken;
      } else {
        throw new Error('토큰 재발급 실패');
      }
    } catch (reissueError) {
      console.error('토큰 재발급 실패:', reissueError);
      AuthToken.clearAccessToken();
      window.location.href = '/login';
      throw reissueError;
    }
  },
};

export default AuthToken;