import { api, Response, PagedResponse } from '@api';
import { Review, ReviewRequest } from '@models/review';

const PAGE_SIZE = 10;

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
    reviewCreateDTO: ReviewRequest
  ): Promise<Response<Review>> {
    try {
      const response = await api.post<Review>(
        `/api/secure/reviews/${webtoonId}`, 
        reviewCreateDTO,
      );
      return { success: true, data: response.data };
    } catch (error) {
      console.error('리뷰 생성 중 오류 발생:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 특정 리뷰 가져오기
  public async getWebtoonReviewById(
    reviewId: number
  ): Promise<Response<Review>> {
    try {
      const response = await api.get<Review>(`/api/public/reviews/${reviewId}`);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('리뷰 가져오기 중 오류 발생:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 리뷰 수정
  public async updateWebtoonReview(
    reviewId: number, 
    reviewCreateDTO: ReviewRequest
  ): Promise<Response<Review>> {
    try {
      const response = await api.put<Review>(
        `/api/secure/reviews/${reviewId}`, 
        reviewCreateDTO, 
      );
      return { success: true, data: response.data };
    } catch (error) {
      console.error('리뷰 수정 중 오류 발생:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 리뷰 삭제
  public async deleteWebtoonReview(
    reviewId: number
  ): Promise<Response<void>> {
    try {
      await api.delete(`/api/secure/reviews/${reviewId}`);
      return { success: true };
    } catch (error) {
      console.error('리뷰 삭제 중 오류 발생:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 좋아요 토글
  public async toggleLikeForReview(
    reviewId: number
  ): Promise<Response<void>> {
    try {
      await api.post(`/api/secure/reviews/${reviewId}/like`);
      return { success: true };
    } catch (error) {
      console.error('리뷰 좋아요 토글 중 오류 발생:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 사용자가 특정 웹툰에 작성한 리뷰 조회
  public async getUserReviewForWebtoon(webtoonId: number): Promise<Response<Review>> {
    try {
      const response = await api.get<Review>(`/api/secure/reviews/${webtoonId}/member`);
      return { success: true, data: response.data };
    } catch (error) {
      console.error('사용자 리뷰 가져오기 중 오류 발생:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }

  // 특정 웹툰의 리뷰 목록 가져오기
  public async getReviewsByWebtoon(
    webtoonId: number, 
    sortBy: string = 'latest', 
    page: number = 0, 
    size: number = 20
  ): Promise<PagedResponse<Review[]>> {
    try {
      const response = await api.get<PagedResponse<Review[]>>(
        `/api/public/reviews/webtoon/${webtoonId}?sortBy=${sortBy}&page=${page}&size=${size}`
      );
      // 응답 데이터 구조 검증
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
      console.error('웹툰 리뷰 목록 가져오기 중 오류 발생:', error);
      return { success: false, message: error instanceof Error ? error.message : 'Unknown error' };
    }
  }
}

export default WebtoonReviewService.getInstance(); 