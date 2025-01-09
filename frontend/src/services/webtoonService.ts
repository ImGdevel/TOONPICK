import api from '@services/ApiService';
import { Webtoon } from '@models/webtoon';
import { DayOfWeek, SerializationStatus, AgeRating, Platform } from '@models/enum';

const PAGE_SIZE = 20;

export interface Response<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  total?: number;
}

export interface PagedResponse<T> extends Response<T> {
  page?: number; 
  size?: number; 
  totalElements?: number; 
  totalPages?: number;
  last?: boolean;
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

  // 요일별 웹툰 목록 조회
  public async getWebtoonsByDayOfWeek(week: DayOfWeek): Promise<PagedResponse<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons`, { params: { week } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 상세 조회
  public async getWebtoonById(id: number): Promise<PagedResponse<Webtoon>> {
    try {
      const response = await api.get<Webtoon>(`/api/public/webtoons/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 완결 웹툰 목록 조회
  public async getWebtoons(
    page: number,
    size: number = PAGE_SIZE,
    sortBy: string = 'title',
    sortDir: 'asc' | 'desc' = 'asc',
    genres?: string[],
    platform?: Platform
  ): Promise<PagedResponse<Webtoon[]>> {
    try {
      const response = await api.get<PagedResponse<Webtoon[]>>(`/api/public/webtoons`, {
        params: {
          page,
          size,
          sortBy,
          sortDir,
          serializationStatus: SerializationStatus.COMPLETED,
          genres,
          platform,
        },
      });

      // 응답 데이터 구조 검증
      const { data, totalElements, page: currentPage, size: pageSize, last } = response.data || {};
      
      return {
        success: true,
        data: data || [],
        total: totalElements || 0, 
        page: currentPage || 0,
        size: pageSize || size,
        last: last || false,
      };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 완결 웹툰 목록 조회
  public async getCompletedWebtoons(
    page: number,
    size: number = PAGE_SIZE,
    sortBy: string = 'title',
    sortDir: 'asc' | 'desc' = 'asc',
    genres?: string[],
    platform?: Platform
  ): Promise<PagedResponse<Webtoon[]>> {
    try {
      const response = await api.get<PagedResponse<Webtoon[]>>(`/api/public/webtoons`, {
        params: {
          page,
          size,
          sortBy,
          sortDir,
          serializationStatus: SerializationStatus.COMPLETED,
          genres,
          platform,
        },
      });

      // 응답 데이터 구조 검증
      const { data, totalElements, page: currentPage, size: pageSize, last } = response.data || {};
      
      return {
        success: true,
        data: data || [],
        total: totalElements || 0, 
        page: currentPage || 0,
        size: pageSize || size,
        last: last || false,
      };
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
  }): Promise<Response<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons`, { params: options });
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
