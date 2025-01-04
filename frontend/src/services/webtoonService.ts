import api from './ApiService';

interface WebtoonResponse<T = any> {
  success: boolean;
  data?: T;
  total?: number;
  message?: string;
  error?: string;
}

interface CarouselImage {
  id: number;
  imageUrl: string;
  title: string;
}

type DayOfWeek = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';

class WebtoonService {
  private static instance: WebtoonService;

  private constructor() {}

  public static getInstance(): WebtoonService {
    if (!WebtoonService.instance) {
      WebtoonService.instance = new WebtoonService();
    }
    return WebtoonService.instance;
  }

  // 연재 중인 모든 웹툰 데이터
  public async getWebtoons(): Promise<WebtoonResponse> {
    try {
      const response = await api.get('/api/webtoons/series', { authRequired: false });
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching webtoons:', error);
      return { success: false, data: [] };
    }
  }

  // 특정 요일의 웹툰 데이터
  public async getWebtoonsByDayOfWeek(dayOfWeek: DayOfWeek): Promise<WebtoonResponse> {
    try {
      const response = await api.get(`/api/webtoons/series/${dayOfWeek}`, { authRequired: false });
      return { success: true, data: response };
    } catch (error) {
      console.error(`Error fetching webtoons for day: ${dayOfWeek}`, error);
      return { 
        success: false, 
        error: error instanceof Error ? error.message : 'Unknown error' 
      };
    }
  }

  // 특정 웹툰 데이터
  public async getWebtoonById(id: number): Promise<WebtoonResponse | null> {
    try {
      const response = await api.get(`/api/webtoons/${id}`, { authRequired: false });
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching webtoon by ID:', error);
      return null;
    }
  }

  // 완결된 웹툰 데이터
  public async getCompletedWebtoons(page: number): Promise<WebtoonResponse> {
    const size = 60;

    try {
      const response = await api.get(
        `/api/webtoons/completed?page=${page}&size=${size}`, 
        { authRequired: false }
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('완결된 웹툰을 가져오는 중 오류가 발생했습니다:', error);
      return { success: false, data: [] };
    }
  }

  // 캐러셀 이미지 데이터
  public async getCarouselImages(): Promise<CarouselImage[]> {
    return [
      { id: 1, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 1' },
      { id: 2, imageUrl: 'https://via.placeholder.com/800x400', title: 'Banner 2' },
    ];
  }

  // 인기 웹툰 조회
  public async getPopularWebtoons(): Promise<WebtoonResponse> {
    try {
      const response = await api.get('/api/webtoons/popular', { authRequired: false });
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching popular webtoons:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 최신 웹툰 조회
  public async getRecentWebtoons(): Promise<WebtoonResponse> {
    try {
      const response = await api.get('/api/webtoons/recent', { authRequired: false });
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching recent webtoons:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 웹툰 검색
  public async searchWebtoons(query: string): Promise<WebtoonResponse> {
    try {
      const response = await api.get('/api/webtoons/search', {
        params: { q: query },
        authRequired: false
      });
      return { success: true, data: response };
    } catch (error) {
      console.error('Error searching webtoons:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 카테고리별 웹툰 조회
  public async getWebtoonsByCategory(category: string): Promise<WebtoonResponse> {
    try {
      const response = await api.get(`/api/webtoons/category/${category}`, { authRequired: false });
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching webtoons by category:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 추천 웹툰 조회
  public async getRecommendedWebtoons(): Promise<WebtoonResponse> {
    try {
      const response = await api.get('/api/webtoons/recommended', { authRequired: false });
      return { success: true, data: response };
    } catch (error) {
      console.error('Error fetching recommended webtoons:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }
}

export default WebtoonService.getInstance(); 