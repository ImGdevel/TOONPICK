import { AuthTokens } from '../types/auth';

export const setStoredTokens = (tokens: AuthTokens) => {
  localStorage.setItem('accessToken', tokens.accessToken);
  localStorage.setItem('refreshToken', tokens.refreshToken);
};

export const getStoredTokens = (): AuthTokens | null => {
  const accessToken = localStorage.getItem('accessToken');
  const refreshToken = localStorage.getItem('refreshToken');
  if (accessToken && refreshToken) {
    return { accessToken, refreshToken };
  }
  return null;
};

export const removeStoredTokens = () => {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
}; 