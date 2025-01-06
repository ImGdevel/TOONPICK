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

  private handleError(error: any): { success: false; message: string } {
    console.error('API Error:', error);
    return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
  }

  public async getWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/series?page=${page}&size=${PAGE_SIZE}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  public async getWebtoonsByDayOfWeek(dayOfWeek: DayOfWeek): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/series/${dayOfWeek}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  public async getWebtoonById(id: number): Promise<WebtoonResponse<Webtoon> | null> {
    try {
      const response = await api.get(`/api/public/webtoons/${id}`);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('Error fetching webtoon by ID:', error);
      return null;
    }
  }

  public async getCompletedWebtoons(page: number): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/completed?page=${page}&size=${COMPLETED_WEBTOON_SIZE}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  public async getPopularWebtoons(): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get('/api/public/webtoons/popular');
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  public async getRecentWebtoons(): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get('/api/public/webtoons/recent');
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

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

  public async getWebtoonsByCategory(category: string): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get(`/api/public/webtoons/category/${category}`);
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }

  public async getRecommendedWebtoons(): Promise<WebtoonResponse<Webtoon[]>> {
    try {
      const response = await api.get('/api/public/webtoons/recommended');
      return { success: true, data: response.data };
    } catch (error) {
      return this.handleError(error);
    }
  }
}

export default WebtoonService.getInstance();
