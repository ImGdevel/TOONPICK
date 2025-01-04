import { AxiosResponse } from 'axios';
import ApiService from './ApiService';
import { 
  Review, 
  CreateReviewDto, 
  UpdateReviewDto,
  ReviewResponse,
  ReviewListResponse 
} from '../types/review';
import axios from 'axios';

class WebtoonReviewService {
  private api = ApiService.getAxiosInstance();

  async getReviews(webtoonId: string): Promise<ReviewListResponse> {
    try {
      const response: AxiosResponse<ReviewListResponse> = 
        await this.api.get(`/webtoons/${webtoonId}/reviews`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch reviews');
    }
  }

  async createReview(webtoonId: string, reviewData: CreateReviewDto): Promise<ReviewResponse> {
    try {
      const response: AxiosResponse<ReviewResponse> = 
        await this.api.post(`/webtoons/${webtoonId}/reviews`, reviewData);
      return response.data;
    } catch (error) {
      throw new Error('Failed to create review');
    }
  }

  async updateReview(
    webtoonId: string, 
    reviewId: string, 
    reviewData: UpdateReviewDto
  ): Promise<ReviewResponse> {
    try {
      const response: AxiosResponse<ReviewResponse> = 
        await this.api.put(`/webtoons/${webtoonId}/reviews/${reviewId}`, reviewData);
      return response.data;
    } catch (error) {
      throw new Error('Failed to update review');
    }
  }

  async deleteReview(webtoonId: string, reviewId: string): Promise<void> {
    try {
      await this.api.delete(`/webtoons/${webtoonId}/reviews/${reviewId}`);
    } catch (error) {
      throw new Error('Failed to delete review');
    }
  }
}

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

export const getUserReviews = async (): Promise<ReviewListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users/reviews`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch user reviews');
  }
};

export const toggleLikeForReview = async (webtoonId: number, reviewId: number) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/webtoons/${webtoonId}/reviews/${reviewId}/like`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to toggle review like');
  }
};

export const reportWebtoonReview = async (reviewId: number, reportData: { reason: string }) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/reviews/${reviewId}/report`, reportData);
    return response.data;
  } catch (error) {
    throw new Error('Failed to report review');
  }
};

export const getLikedReviews = async (webtoonId: number) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/${webtoonId}/reviews/liked`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch liked reviews');
  }
};

export default new WebtoonReviewService(); 