import { Webtoon } from './webtoon';
import { User } from './auth';
import { Review } from './review';

export interface PaginationParams {
  page: number;
  limit: number;
}

export interface WebtoonListPageProps {
  category?: string;
  searchQuery?: string;
}

export interface SearchPageProps {
  initialQuery?: string;
}

export interface WebtoonDetailPageProps {
  id: string;
}

export interface HomePageState {
  popularWebtoons: Webtoon[];
  recentWebtoons: Webtoon[];
  isLoading: boolean;
  error: string | null;
}

export interface WebtoonListPageState {
  webtoons: Webtoon[];
  totalPages: number;
  currentPage: number;
  isLoading: boolean;
  error: string | null;
}

export interface MyPageState {
  user: User | null;
  bookmarks: Webtoon[];
  favorites: Webtoon[];
  reviews: Review[];
  isLoading: boolean;
  error: string | null;
}

export interface ExplorePageState {
  categories: string[];
  selectedCategory: string | null;
  webtoons: Webtoon[];
  isLoading: boolean;
  error: string | null;
}

export interface WebtoonsPageState {
  webtoons: Webtoon[];
  currentPage: number;
  totalPages: number;
  isLoading: boolean;
  error: string | null;
} 