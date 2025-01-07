import api from '@services/ApiService';
import { Webtoon } from '@models/webtoon';
import { DayOfWeek, SerializationStatus, AgeRating, Platform } from '@models/enum';

const PAGE_SIZE = 20;
const COMPLETED_WEBTOON_SIZE = 60;

export interface WebtoonResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  total?: number;
}

// WebtoonService 클래스
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
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/filter`, { params: { page, size: PAGE_SIZE } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 요일별 웹툰 목록 조회
  public async getWebtoonsByDayOfWeek(week: DayOfWeek): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/filter`, { params: { week } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 상세 조회
  public async getWebtoonById(id: number): Promise<WebtoonResponse<Webtoon>> {
    try {
      const response = await api.get<Webtoon>(`/api/public/webtoons/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 완결 웹툰 목록 조회
  public async getCompletedWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/completed`, { params: { page, size: COMPLETED_WEBTOON_SIZE } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 인기 웹툰 목록 조회
  public async getPopularWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/popular`, { params: { page, size: 60 } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 최근 웹툰 목록 조회
  public async getRecentWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/recent`, { params: { page, size: PAGE_SIZE } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 검색
  public async searchWebtoons(query: string): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/filter`, { params: { title: query } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 필터 기반 웹툰 조회
  public async filterWebtoons(options: {
    platform?: Platform;
    serializationStatus?: SerializationStatus;
    ageRating?: AgeRating;
    week?: DayOfWeek;
    genres?: string[];
    authors?: string[];
    page?: number;
    size?: number;
    sortBy?: string;
    sortDir?: 'asc' | 'desc';
  }): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/filter`, { params: options });
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
