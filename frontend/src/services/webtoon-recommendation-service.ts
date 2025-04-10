import { api, Response, PagedResponse } from '@api';
import { Webtoon, Platform, SerializationStatus } from '@models/webtoon';
import { DUMMY_WEBTOON } from '@constants/dummy-data';

// 테스트 모드 전역 변수
export const testActive = process.env.REACT_APP_TEST_MODE === 'true';

class WebtoonRecommendationService {
  private static instance: WebtoonRecommendationService;

  private constructor() {}

  public static getInstance(): WebtoonRecommendationService {
    if (!WebtoonRecommendationService.instance) {
      WebtoonRecommendationService.instance = new WebtoonRecommendationService();
    }
    return WebtoonRecommendationService.instance;
  }

  // 사용자가 보지 않은 인기 웹툰 목록 조회
  public async getPopularUnreadWebtoons(
    userId: number,
    options: {
      page: number;
      size?: number;
      sortBy?: string;
      sortDir?: 'asc' | 'desc';
      platform?: Platform;
      genres?: string[];
      serializationStatus?: SerializationStatus;
    }
  ): Promise<{ data: Webtoon[]; total: number; last: boolean }> {
    if (testActive) {
      // 테스트 데이터에서는 사용자가 보지 않은 웹툰을 시뮬레이션하기 위해
      // 더미 웹툰의 ID를 변경하여 반환합니다.
      const unreadWebtoons = Array(10).fill(null).map((_, index) => ({
        ...DUMMY_WEBTOON,
        id: index + 100, // 원본 더미 웹툰과 다른 ID 사용
        title: `추천 웹툰 ${index + 1}`,
        averageRating: 4 + Math.random() * 1, // 4.0 ~ 5.0 사이의 평점
        totalRatings: Math.floor(100 + Math.random() * 900) // 100 ~ 1000 사이의 평가 수
      }));

      // 평점 순으로 정렬
      unreadWebtoons.sort((a, b) => b.averageRating - a.averageRating);

      // 페이지네이션 적용
      const pageSize = options.size || 10;
      const startIndex = options.page * pageSize;
      const endIndex = startIndex + pageSize;
      const paginatedWebtoons = unreadWebtoons.slice(startIndex, endIndex);

      return {
        data: paginatedWebtoons,
        total: unreadWebtoons.length,
        last: endIndex >= unreadWebtoons.length
      };
    }

    try {
      const response = await api.get<PagedResponse<Webtoon[]>>(`/api/public/webtoons/recommendations/unread`, {
        params: {
          userId,
          page: options.page,
          size: options.size || 10,
          sortBy: options.sortBy || 'rating',
          sortDir: options.sortDir || 'desc',
          platform: options.platform,
          genres: options.genres,
          serializationStatus: options.serializationStatus,
        },
      });

      if (response.data.success && response.data.data) {
        return {
          data: response.data.data,
          total: response.data.total || 0,
          last: response.data.last || false
        };
      }
      throw new Error('Failed to fetch unread webtoons');
    } catch (error) {
      console.error('Failed to fetch unread webtoons:', error);
      throw error;
    }
  }
}

export default WebtoonRecommendationService.getInstance(); 