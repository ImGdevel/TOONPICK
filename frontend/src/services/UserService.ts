import api, { CustomAxiosRequestConfig } from './ApiService';

interface UserResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
}

class UserService {
  private static instance: UserService;

  private constructor() {}

  public static getInstance(): UserService {
    if (!UserService.instance) {
      UserService.instance = new UserService();
    }
    return UserService.instance;
  }

  // 사용자 프로필 정보 가져오기
  public async getUserProfile(): Promise<UserResponse> {
    try {
      const response = await api.get('/api/user/profile', { authRequired: true } as CustomAxiosRequestConfig);
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching user profile:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 관심 웹툰 추가
  public async addFavoriteWebtoon(webtoonId: number): Promise<UserResponse> {
    try {
      await api.post(`/api/users/favorites/${webtoonId}`, null, { authRequired: true } as CustomAxiosRequestConfig);
      return { success: true };
    } catch (error) {
      console.error('Error adding favorite webtoon:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 관심 웹툰 삭제
  public async removeFavoriteWebtoon(webtoonId: number): Promise<UserResponse> {
    try {
      await api.delete(`/api/users/favorites/${webtoonId}`, { authRequired: true } as CustomAxiosRequestConfig);
      return { success: true };
    } catch (error) {
      console.error('Error removing favorite webtoon:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 관심 웹툰 목록 가져오기
  public async getFavorites(): Promise<UserResponse> {
    try {
      const response = await api.get(`/api/users/favorites`, { authRequired: true } as CustomAxiosRequestConfig);
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching favorites:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 웹툰이 관심 웹툰인지 확인
  public async isFavoriteWebtoon(webtoonId: number): Promise<UserResponse<boolean>> {
    try {
      const response = await api.get<boolean>(`/api/users/favorites/${webtoonId}/is-favorite`, { authRequired: true } as CustomAxiosRequestConfig);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error checking favorite webtoon:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  public async getBookmarks(): Promise<UserResponse> {
    try {
      const response = await api.get('/api/users/bookmarks', { authRequired: true } as CustomAxiosRequestConfig);
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching bookmarks:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }
}

export default UserService.getInstance();