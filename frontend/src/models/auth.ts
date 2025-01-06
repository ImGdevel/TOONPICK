import { ApiResponse } from './api';

export interface User {
  id: string;
  email: string;
  username: string;
  profileImage?: string;
  createdAt: string;
  updatedAt: string;
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterCredentials extends LoginCredentials {
  username: string;
}

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
}

export interface AuthResponse extends ApiResponse<{
  user: User;
  tokens: AuthTokens;
}> {}

export interface UserResponse extends ApiResponse<User> {} 