import axios, { AxiosResponse } from 'axios';
import { Webtoon, Platform, StatusType } from '../types/webtoon';
import { WebtoonResponse, WebtoonListResponse, FavoriteResponse, BookmarkResponse, RecommendationResponse } from '../types/api';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

// 웹툰 상세 정보 조회
export const getWebtoonById = async (id: string): Promise<WebtoonResponse> => {
  try {
    const response: AxiosResponse<WebtoonResponse> = await axios.get(
      `${API_BASE_URL}/webtoons/${id}`
    );
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch webtoon details');
  }
};

// 웹툰 목록 조회
export const getWebtoonList = async (
  page: number = 1,
  limit: number = 20
): Promise<WebtoonListResponse> => {
  try {
    const response: AxiosResponse<WebtoonListResponse> = await axios.get(
      `${API_BASE_URL}/webtoons`,
      {
        params: {
          page,
          limit,
        },
      }
    );
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch webtoon list');
  }
};

// 좋아요 상태 확인
export const isFavoriteWebtoon = async (id: string): Promise<boolean> => {
  try {
    const response: AxiosResponse<FavoriteResponse> = await axios.get(
      `${API_BASE_URL}/webtoons/${id}/favorite`
    );
    if (response && response.data && response.data.data) {
      return response.data.data.isFavorite;
    }
    return false;
  } catch (error) {
    return false;
  }
};

// 좋아요 추가
export const addFavoriteWebtoon = async (webtoonId: string): Promise<boolean> => {
  try {
    const response: AxiosResponse<FavoriteResponse> = await axios.post(
      `${API_BASE_URL}/webtoons/${webtoonId}/favorite`
    );
    return response.data.success;
  } catch (error) {
    throw new Error('Failed to add favorite');
  }
};

// 좋아요 제거
export const removeFavoriteWebtoon = async (webtoonId: string): Promise<boolean> => {
  try {
    const response: AxiosResponse<FavoriteResponse> = await axios.delete(
      `${API_BASE_URL}/webtoons/${webtoonId}/favorite`
    );
    return response.data.success;
  } catch (error) {
    throw new Error('Failed to remove favorite');
  }
};

// 북마크 상태 확인
export const isBookmarkedWebtoon = async (webtoonId: string): Promise<boolean> => {
  try {
    const response: AxiosResponse<BookmarkResponse> = await axios.get(
      `${API_BASE_URL}/webtoons/${webtoonId}/bookmark`
    );
    return response.data.data.isBookmarked;
  } catch (error) {
    return false;
  }
};

// 북마크 추가
export const addBookmarkWebtoon = async (webtoonId: string): Promise<boolean> => {
  try {
    const response: AxiosResponse<BookmarkResponse> = await axios.post(
      `${API_BASE_URL}/webtoons/${webtoonId}/bookmark`
    );
    return response.data.success;
  } catch (error) {
    throw new Error('Failed to add bookmark');
  }
};

// 북마크 제거
export const removeBookmarkWebtoon = async (webtoonId: string): Promise<boolean> => {
  try {
    const response: AxiosResponse<BookmarkResponse> = await axios.delete(
      `${API_BASE_URL}/webtoons/${webtoonId}/bookmark`
    );
    return response.data.success;
  } catch (error) {
    throw new Error('Failed to remove bookmark');
  }
};

// 인기 웹툰 조회
export const getPopularWebtoons = async (): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/popular`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch popular webtoons');
  }
};

// 최신 웹툰 조회
export const getRecentWebtoons = async (): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/recent`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch recent webtoons');
  }
};

// 추천 웹툰 조회
export const getRecommendedWebtoons = async (): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/recommended`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch recommended webtoons');
  }
};

// 웹툰 검색
export const searchWebtoons = async (query: string): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/search`, {
      params: { q: query },
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to search webtoons');
  }
};

// 추천 웹툰 조회 (타입별)
export const getRecommendations = async (type: 'personalized' | 'popular' | 'similar'): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/recommendations/${type}`);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to fetch ${type} recommendations`);
  }
};

// 신작 웹툰 조회
export const getNewWebtoons = async (page: number = 1): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/new`, {
      params: { page }
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch new webtoons');
  }
};

// 북마크 목록 조회
export const getBookmarks = async (): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users/bookmarks`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch bookmarks');
  }
};

// 좋아요 목록 조회
export const getFavorites = async (): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/users/favorites`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch favorites');
  }
};

// 완결 웹툰 조회
export const getCompletedWebtoons = async (page: number = 1): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/completed`, {
      params: { page }
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch completed webtoons');
  }
};

// 카테고리 목록 조회
export const getCategories = async (): Promise<{ data: string[] }> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/categories`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch categories');
  }
};

// 카테고리별 웹툰 조회
export const getWebtoonsByCategory = async (category: string): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/category/${category}`);
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch webtoons by category');
  }
};

// 연재 중인 웹툰 조회
export const getOngoingWebtoons = async (page: number = 1): Promise<WebtoonListResponse> => {
  try {
    const response = await axios.get(`${API_BASE_URL}/webtoons/ongoing`, {
      params: { page }
    });
    return response.data;
  } catch (error) {
    throw new Error('Failed to fetch ongoing webtoons');
  }
}; 