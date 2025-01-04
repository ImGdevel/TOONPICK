import { User } from './auth';
import { ApiResponse } from './api';

export interface Review {
  id: string;
  webtoonId: string;
  userId: string;
  content: string;
  rating: number;
  createdAt: string;
  updatedAt: string;
  user?: User;
}

export interface CreateReviewDto {
  content: string;
  rating: number;
}

export interface UpdateReviewDto {
  content?: string;
  rating?: number;
}

export interface ReviewResponse extends ApiResponse<Review> {}
export interface ReviewListResponse extends ApiResponse<Review[]> {} 