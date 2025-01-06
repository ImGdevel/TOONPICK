import api from '@services/ApiService';
import { Webtoon } from '@models/webtoon';
import { DayOfWeek, SerializationStatus, AgeRating, Platform } from '@models/enum';

const PAGE_SIZE = 20;
const COMPLETED_WEBTOON_SIZE = 60;


type WebtoonResponse<T> = {
  success: boolean;
  data?: T;
  message?: string;
  total?: number;
};


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

  // 공통 API 호출 메서드
  private async fetchFromAPI<T>(url: string, params?: Record<string, any>): Promise<WebtoonResponse<T>> {
    try {
      const response = await api.get(url, { params });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 목록 조회
  public async getWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    return this.fetchFromAPI<Webtoon[]>(`/api/public/webtoons/filter`, { page, size: PAGE_SIZE });
  }

  // 요일별 웹툰 목록 조회
  public async getWebtoonsByDayOfWeek(week: DayOfWeek): Promise<WebtoonResponse<Webtoon[]>> {
    return this.fetchFromAPI<Webtoon[]>(`/api/public/webtoons/filter`, { week });
  }

  // 웹툰 상세 조회
  public async getWebtoonById(id: number): Promise<WebtoonResponse<Webtoon>> {
    return this.fetchFromAPI<Webtoon>(`/api/public/webtoons/${id}`);
  }

  // 완결 웹툰 목록 조회
  public async getCompletedWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    return this.fetchFromAPI<Webtoon[]>(`/api/public/webtoons/completed`, { page, size: COMPLETED_WEBTOON_SIZE });
  }

  // 인기 웹툰 목록 조회
  public async getPopularWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    return this.fetchFromAPI<Webtoon[]>(`/api/public/webtoons/popular`, { page, size: 60 });
  }

  // 최근 웹툰 목록 조회
  public async getRecentWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    return this.fetchFromAPI<Webtoon[]>(`/api/public/webtoons/recent`, { page, size: PAGE_SIZE });
  }

  // 웹툰 검색
  public async searchWebtoons(query: string): Promise<WebtoonResponse<Webtoon[]>> {
    return this.fetchFromAPI<Webtoon[]>(`/api/public/webtoons/filter`, { title: query });
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
    return this.fetchFromAPI<Webtoon[]>(`/api/public/webtoons/filter`, options);
  }

  // 오류 처리
  private handleError(error: any): { success: false; message: string } {
    console.error('API Error:', error);
    return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
  }
}

export default WebtoonService.getInstance();
