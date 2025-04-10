import { api, Response, PagedResponse } from '@api';
import { Webtoon, Platform, SerializationStatus } from '@models/webtoon';
import { DayOfWeek, AgeRating } from '@models/enum';
import { DUMMY_WEBTOON } from '@constants/dummy-data';

const PAGE_SIZE = 20;

// 테스트 모드 전역 변수
export const testActive = process.env.REACT_APP_TEST_MODE === 'true';

// WebtoonService 클래스
class WebtoonService {
  private static instance: WebtoonService;

  private constructor() {}

  public static getInstance(): WebtoonService {
    if (!WebtoonService.instance) {
      WebtoonService.instance = new WebtoonService();
    }
    return WebtoonService.instance;
  }

  // 웹툰 상세 조회
  public async getWebtoonById(id: number): Promise<Webtoon> {
    if (testActive) {
      return {
        ...DUMMY_WEBTOON,
        id
      };
    }

    try {
      const response = await api.get<Response<Webtoon>>(`/api/public/webtoons/${id}`);
      if (response.data.success && response.data.data) {
        return response.data.data;
      }
      throw new Error('Failed to fetch webtoon');
    } catch (error) {
      console.error('Failed to fetch webtoon:', error);
      throw error;
    }
  }

  // 웹툰 목록 조회
  public async getWebtoons(
    options: {
      page: number;
      size?: number;
      sortBy?: string;
      sortDir?: 'asc' | 'desc';
      platform?: Platform;
      genres?: string[];
      authors?: string[];
      week?: DayOfWeek;
      serializationStatus?: SerializationStatus;
      ageRating?: AgeRating;
    }
  ): Promise<{ data: Webtoon[]; total: number; last: boolean }> {
    if (testActive) {
      return {
        data: [DUMMY_WEBTOON],
        total: 1,
        last: true
      };
    }

    try {
      const response = await api.get<PagedResponse<Webtoon[]>>(`/api/public/webtoons`, {
        params: {
          page: options.page,
          size: options.size || PAGE_SIZE,
          sortBy: options.sortBy || 'title',
          sortDir: options.sortDir || 'asc',
          genres: options.genres,
          authors: options.authors,
          platform: options.platform,
          week: options.week,
          serializationStatus: options.serializationStatus,
          ageRating: options.ageRating,
        },
      });

      if (response.data.success && response.data.data) {
        return {
          data: response.data.data,
          total: response.data.total || 0,
          last: response.data.last || false
        };
      }
      throw new Error('Failed to fetch webtoons');
    } catch (error) {
      console.error('Failed to fetch webtoons:', error);
      throw error;
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
  ): Promise<{ data: Webtoon[]; total: number; last: boolean }> {
    if (testActive) {
      return {
        data: [DUMMY_WEBTOON],
        total: 1,
        last: true
      };
    }

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

      if (response.data.success && response.data.data) {
        return {
          data: response.data.data,
          total: response.data.total || 0,
          last: response.data.last || false
        };
      }
      throw new Error('Failed to fetch completed webtoons');
    } catch (error) {
      console.error('Failed to fetch completed webtoons:', error);
      throw error;
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
  }): Promise<Webtoon[]> {
    if (testActive) {
      return [DUMMY_WEBTOON];
    }

    try {
      const response = await api.get<Response<Webtoon[]>>(`/api/public/webtoons`, { params: options });
      if (response.data.success && response.data.data) {
        return response.data.data;
      }
      throw new Error('Failed to filter webtoons');
    } catch (error) {
      console.error('Failed to filter webtoons:', error);
      throw error;
    }
  }
}

export default WebtoonService.getInstance();
