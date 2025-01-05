import api, { CustomAxiosRequestConfig } from './ApiService';

interface ReviewResponse<T = any> {
  success: boolean;
  data?: T;
  error?: string;
}

interface ReviewData {
  rating: number;
  comment: string;
}

interface ReportData {
  reason: string;
  description?: string;
}

class WebtoonReviewService {
  private static instance: WebtoonReviewService;

  private constructor() {}

  public static getInstance(): WebtoonReviewService {
    if (!WebtoonReviewService.instance) {
      WebtoonReviewService.instance = new WebtoonReviewService();
    }
    return WebtoonReviewService.instance;
  }

  // 웹툰 리뷰 생성
  public async createWebtoonReview(
    webtoonId: number, 
    rating: number, 
    comment: string
  ): Promise<ReviewResponse> {
    try {
      const reviewData: ReviewData = { rating, comment };
      const response = await api.post(
        `/api/webtoon/${webtoonId}/reviews`, 
        reviewData, 
        { authRequired: true } as CustomAxiosRequestConfig
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('리뷰 생성 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 특정 리뷰 가져오기
  public async getWebtoonReviewById(
    webtoonId: number, 
    reviewId: number
  ): Promise<ReviewResponse> {
    try {
      const response = await api.get(`/api/webtoon/${webtoonId}/reviews/${reviewId}`);
      return { success: true, data: response };
    } catch (error) {
      console.error('리뷰 가져오기 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 리뷰 수정
  public async updateWebtoonReview(
    webtoonId: number, 
    reviewId: number, 
    rating: number, 
    comment: string
  ): Promise<ReviewResponse> {
    try {
      const reviewData: ReviewData = { rating, comment };
      const response = await api.put(
        `/api/webtoon/${webtoonId}/reviews/${reviewId}`, 
        reviewData, 
        { authRequired: true } as CustomAxiosRequestConfig
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('리뷰 수정 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 리뷰 삭제
  public async deleteWebtoonReview(
    webtoonId: number, 
    reviewId: number
  ): Promise<ReviewResponse> {
    try {
      await api.delete(`/api/webtoon/${webtoonId}/reviews/${reviewId}`);
      return { success: true };
    } catch (error) {
      console.error('리뷰 삭제 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 리뷰 좋아요 토글
  public async toggleLikeForReview(
    webtoonId: number, 
    reviewId: number
  ): Promise<ReviewResponse> {
    try {
      const response = await api.post(
        `/api/webtoon/${webtoonId}/reviews/${reviewId}/like`, 
        null, 
        { authRequired: true } as CustomAxiosRequestConfig
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('리뷰 좋아요 토글 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 좋아요 상태 조회
  public async getLikedReviews(webtoonId: number): Promise<ReviewResponse> {
    try {
      const response = await api.get(
        `/api/webtoon/${webtoonId}/reviews/liked-reviews`, 
        { authRequired: true } as CustomAxiosRequestConfig
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('좋아요 상태 조회 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 특정 웹툰의 리뷰 목록 가져오기
  public async getReviewsByWebtoon(
    webtoonId: number, 
    sortBy: string = 'latest', 
    page: number = 0, 
    size: number = 20
  ): Promise<ReviewResponse> {
    try {
      const response = await api.get(
        `/api/webtoon/${webtoonId}/reviews?sortBy=${sortBy}&page=${page}&size=${size}`
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('웹툰 리뷰 목록 가져오기 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 특정 웹툰에 대한 사용자 리뷰 가져오기
  public async getUserReviewForWebtoon(webtoonId: number): Promise<ReviewResponse> {
    try {
      const response = await api.get(
        `/api/webtoon/${webtoonId}/reviews/users`, 
        { authRequired: true } as CustomAxiosRequestConfig
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('사용자 리뷰 가져오기 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 리뷰 신고
  public async reportWebtoonReview(
    webtoonId: number, 
    reviewId: number, 
    reportData: ReportData
  ): Promise<ReviewResponse> {
    try {
      const response = await api.post(
        `/api/webtoon/${webtoonId}/reviews/${reviewId}/report`, 
        reportData, 
        { authRequired: true } as CustomAxiosRequestConfig
      );
      return { success: true, data: response };
    } catch (error) {
      console.error('리뷰 신고 중 오류 발생:', error);
      return { success: false, error: error instanceof Error ? error.message : 'Unknown error' };
    }
  }
}

export default WebtoonReviewService.getInstance(); 