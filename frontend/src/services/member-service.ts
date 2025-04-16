import { api, Response } from '@api';
import { MemberProfile } from '@models/member';
import { Webtoon } from '@models/webtoon';


class MemberService {
  private static instance: MemberService;

  private constructor() {}

  public static getInstance(): MemberService {
    if (!MemberService.instance) {
      MemberService.instance = new MemberService();
    }
    return MemberService.instance;
  }

  // 사용자 프로필 정보 가져오기
  public async getMemberProfile(): Promise<Response<MemberProfile>> {
    try {
      const response = await api.get<MemberProfile>('/api/secure/member/profile');
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error fetching member profile:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 관심 웹툰 추가
  public async addFavoriteWebtoon(webtoonId: number): Promise<Response> {
    try {
      await api.post(`/api/secure/member/favorites/${webtoonId}`);
      return { success: true };
    } catch (error) {
      console.error('Error adding favorite webtoon:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 관심 웹툰 삭제
  public async removeFavoriteWebtoon(webtoonId: number): Promise<Response> {
    try {
      await api.delete(`/api/secure/member/favorites/${webtoonId}`);
      return { success: true };
    } catch (error) {
      console.error('Error removing favorite webtoon:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 관심 웹툰 목록 가져오기
  public async getFavorites(): Promise<Response<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/secure/member/favorites`);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error fetching favorites:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 웹툰이 관심 웹툰인지 확인
  public async isFavoriteWebtoon(webtoonId: number): Promise<Response<boolean>> {
    try {
      const response = await api.get<boolean>(`/api/secure/member/favorites/${webtoonId}/is-favorite`);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error checking favorite webtoon:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }
}

export default MemberService.getInstance();