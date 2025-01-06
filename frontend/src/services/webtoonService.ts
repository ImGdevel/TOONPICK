import api from '@services/ApiService';
import { Webtoon, WebtoonResponse } from '@models/webtoon';


type DayOfWeek = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';

const PAGE_SIZE = 20;
const COMPLETED_WEBTOON_SIZE = 60;

class WebtoonService {
  private static instance: WebtoonService;

  private constructor() {}

  public static getInstance(): WebtoonService {
    if (!this.instance) {
      this.instance = new WebtoonService();
    }
    return this.instance;
  }

  // 웹툰 목록 조회
  public async getWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/series?page=${page}&size=${PAGE_SIZE}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 요일별 웹툰 목록 조회
  public async getWebtoonsByDayOfWeek(dayOfWeek: DayOfWeek): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/series/${dayOfWeek}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 상세 조회
  public async getWebtoonById(id: number): Promise<WebtoonResponse<Webtoon> | null> {
    try {
      const response = await api.get(`/api/public/webtoons/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error fetching webtoon by ID:', error);
      return null;
    }
  }

  // 완결 웹툰 목록 조회
  public async getCompletedWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/completed?page=${page}&size=${COMPLETED_WEBTOON_SIZE}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 인기 웹툰 목록 조회
  public async getPopularWebtoons(): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get('/api/public/webtoons/popular');
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 최근 웹툰 목록 조회
  public async getRecentWebtoons(): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get('/api/public/webtoons/recent');
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 검색
  public async searchWebtoons(query: string): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get('/api/public/webtoons/search', {
        params: { q: query }
      });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 카테고리별 웹툰 목록 조회
  public async getWebtoonsByCategory(category: string): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/category/${category}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 추천 웹툰 목록 조회
  public async getRecommendedWebtoons(): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get('/api/public/webtoons/recommended');
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 오류 처리
  private handleError(error: any): { success: false; message: string } {
    console.error('API Error:', error);
    return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
  }
}

export default WebtoonService.getInstance();
