const ACCESS_TOKEN_KEY = 'accessToken';

export const TokenManager = {
  /**
   * Store access token in localStorage.
   * @param token - The access token to store.
   */
  setAccessToken: (token: string): void => {
    try {
      localStorage.setItem(ACCESS_TOKEN_KEY, token);
    } catch (error) {
      console.error('Failed to store access token:', error);
    }
  },

  /**
   * Retrieve the access token from localStorage.
   * @returns The access token, or null if not found.
   */
  getAccessToken: (): string | null => {
    try {
      return localStorage.getItem(ACCESS_TOKEN_KEY);
    } catch (error) {
      console.error('Failed to retrieve access token:', error);
      return null;
    }
  },

  /**
   * Remove the access token from localStorage.
   */
  clearAccessToken: (): void => {
    try {
      localStorage.removeItem(ACCESS_TOKEN_KEY);
    } catch (error) {
      console.error('Failed to clear access token:', error);
    }
  },

  /**
   * Remove the refresh token from localStorage.
   */
  clearRefreshToken: (): void => {
    try {
      // todo : 리프레시 토큰 삭제 로직 추가 (서버로 요청)
    } catch (error) {
      console.error('Failed to clear refresh token:', error);
    }
  },

  /**
   * Check if the access token is expired.
   * @param token - The access token to check.
   * @returns True if the token is expired or invalid, false otherwise.
   */
  isAccessTokenExpired: (token: string): boolean => {
    try {
      const payload = TokenManager.decodeTokenPayload(token);
      if (!payload || !payload.exp) {
        console.warn('Invalid token payload: Missing "exp" field.');
        return true;
      }
      return payload.exp * 1000 < Date.now();
    } catch (error) {
      console.error('Error checking token expiration:', error);
      return true;
    }
  },

  /**
   * Extract the access token from the Authorization header.
   * @param headers - The headers object, typically from an HTTP response.
   * @returns The extracted access token, or null if not found.
   */
  extractAccessTokenFromHeader: (headers: Record<string, any>): string | null => {
    try {
      const authHeader = headers['authorization'] || headers['Authorization'];
      if (authHeader && typeof authHeader === 'string') {
        const token = authHeader.split(' ')[1];
        if (!token) {
          console.warn('Authorization header is present but does not contain a valid token.');
        }
        return token || null;
      }
      return null;
    } catch (error) {
      console.error('Failed to extract access token from header:', error);
      return null;
    }
  },

  /**
   * Decode the payload of a JWT token.
   * @param token - The JWT token to decode.
   * @returns The decoded payload as an object, or null if decoding fails.
   */
  decodeTokenPayload: (token: string): Record<string, any> | null => {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Failed to decode token payload:', error);
      return null;
    }
  },
};

export default TokenManager;
