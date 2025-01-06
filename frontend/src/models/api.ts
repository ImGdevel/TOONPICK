import { Webtoon } from './webtoon';

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  error?: string;
}

export interface WebtoonListResponse {
  data: Webtoon[];
  total: number;
}

export interface FavoriteResponse extends ApiResponse<{
  isFavorite: boolean;
}> {}

export interface BookmarkResponse extends ApiResponse<{
  isBookmarked: boolean;
}> {}

export interface RecommendationResponse {
  data: Webtoon[];
  message: string;
}