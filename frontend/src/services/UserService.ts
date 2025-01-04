import { AxiosResponse } from 'axios';
import ApiService from './ApiService';
import { User, UserResponse } from '../types/auth';
import axios from 'axios';

class UserService {
  private api = ApiService.getAxiosInstance();

  async updateProfile(userId: string, data: Partial<User>): Promise<UserResponse> {
    try {
      const response: AxiosResponse<UserResponse> = await this.api.put(`/users/${userId}`, data);
      return response.data;
    } catch (error) {
      throw new Error('Failed to update profile');
    }
  }

  async getUserProfile(userId: string): Promise<UserResponse> {
    try {
      const response: AxiosResponse<UserResponse> = await this.api.get(`/users/${userId}`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to get user profile');
    }
  }

  async deleteAccount(userId: string): Promise<void> {
    try {
      await this.api.delete(`/users/${userId}`);
    } catch (error) {
      throw new Error('Failed to delete account');
    }
  }
}

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

export const getUserProfile = async (): Promise<UserResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users/profile`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch user profile');
  }
};

export default new UserService(); 