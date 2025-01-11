const ACCESS_TOKEN_KEY = 'accessToken';

export const TokenManager = {
  setAccessToken: (token: string): void => {
    localStorage.setItem(ACCESS_TOKEN_KEY, token);
  },

  getAccessToken: (): string | null => {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
  },

  clearAccessToken: (): void => {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
  },

  isAccessTokenExpired: (token: string): boolean => {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 < Date.now();
    } catch (error) {
      console.error('Invalid token format or decoding error:', error);
      return true;
    }
  },

  extractAccessTokenFromHeader: (headers: any): string | null => {
    const authHeader = headers['authorization'];
    if (authHeader && typeof authHeader === 'string') {
      return authHeader.split(' ')[1] || null;
    }
    return null;
  },
};

export default TokenManager; 