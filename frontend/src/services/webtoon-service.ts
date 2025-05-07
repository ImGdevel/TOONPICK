import { api , Response, PagedResponse } from '@api';
import { Webtoon, Platform, SerializationStatus } from '@models/webtoon';
import { DayOfWeek, AgeRating } from '@models/enum';

const PAGE_SIZE = 60;

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

  // 웹툰 간단 정보 조회
  public async getWebtoonById(id: number): Promise<Response<Webtoon>> {
    try {
      const response = await api.get<Webtoon>(`/api/public/webtoons/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 상제 정보 조회
  public async getWebtoonDetails(id: number): Promise<Response<Webtoon>> {
    try {
      const response = await api.get<Webtoon>(`/api/public/webtoons/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 웹툰 목록 조회
  public async getWebtoons(
    options: {
      page: number;
      size?: number;
      sortBy?: string;
      sortDir?: 'asc' | 'desc';
      platforms?: Platform[];
      genres?: string[];
      authors?: string[];
      publishDays?: DayOfWeek[];
      serializationStatuses?: SerializationStatus[];
      ageRatings?: AgeRating[];
    }
  ): Promise<PagedResponse<Webtoon[]>> {
    try {
      const response = await api.post<PagedResponse<Webtoon[]>>('/api/public/webtoons/filter', 
        {
          platforms: options.platforms,
          genres: options.genres,
          authors: options.authors,
          publishDays: options.publishDays,
          serializationStatuses: options.serializationStatuses,
          ageRatings: options.ageRatings
        },
        {
          params: {
            page: options.page,
            size: options.size || PAGE_SIZE,
            sortBy: options.sortBy || 'title',
            sortDir: options.sortDir || 'asc',
          },
        }
      );

      const { data, totalElements, page: currentPage, size: pageSize, last } = response.data || {};

      return {
        success: true,
        data: data || [],
        total: totalElements || 0,
        page: currentPage || 0,
        size: pageSize || PAGE_SIZE,
        last: last || false,
      };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 인기 웹툰 조회
  public async getPopularWebtoons(size: number = 10): Promise<Response<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/popular`, { params: { size } });
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  // 최신 웹툰 조회
  public async getRecentWebtoons(size: number = 10): Promise<Response<Webtoon[]>> {
    try {
      const response = await api.get<Webtoon[]>(`/api/public/webtoons/recent`, { params: { size } });
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
