export enum Platform {
  NAVER = 'NAVER',
  KAKAO = 'KAKAO',
  KAKAOPAGE = 'KAKAOPAGE',
  LEZHIN = 'LEZHIN',
  BOMTOON = 'BOMTOON'
}

export type AgeRating = 'ALL' | 'TEEN' | 'ADULT';
export type DayOfWeek = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';

export enum SerializationStatus {
  ONGOING = 'ONGOING',
  COMPLETED = 'COMPLETED',
  HIATUS = 'HIATUS',
  ENDED = 'ENDED',
  PAUSED = 'PAUSED',
  UNKNOWN = 'UNKNOWN',
}

export interface Webtoon {
  id: number;
  title: string;
  thumbnailUrl: string;
  platform: Platform;
  isAdult: boolean;
  status: SerializationStatus;
  publishDay: string;
  authors: Author[];
  description: string;
  genres: Genre[];
  totalRatings: number;
  averageRating: number;
  similarWebtoons: SimilarWebtoon[] | null;
  analysisData?: WebtoonAnalysisData | null;
}

export interface Author {
  id: number;
  role : string;
  name: string;
}

export interface Genre {
  id: number;
  name: string;
}

export interface SimilarWebtoon {
  id: number;
  title: string;
  thumbnailUrl: string;
  platform: Platform;
  status: SerializationStatus;
  isAdult: boolean;
  averageRating: number;
  genres: Genre[];
}

export interface WebtoonResponse<T = any> {
  success: boolean;
  data?: T;
  total?: number;
  message?: string;
  error?: string;
}

export interface WebtoonAnalysisData {
  totalViews: number;
  totalSubscribers: number;
  averageViewTime: number; // 분 단위
  completionRate: number; // 완독률 (%)
  readerDemographics: {
    ageGroups: { age: string; percentage: number }[];
    genderDistribution: { gender: string; percentage: number }[];
    regionDistribution: { region: string; percentage: number }[];
  };
  ratingDistribution: { rating: number; count: number }[];
  reviewSentiment: {
    positive: number;
    neutral: number;
    negative: number;
  };
  contentAnalysis: {
    genreDistribution: { genre: string; percentage: number }[];
    tagDistribution: { tag: string; count: number }[];
    characterPopularity: { character: string; popularity: number }[];
  };
  growthMetrics: {
    dailyViews: { date: string; count: number }[];
    dailySubscribers: { date: string; count: number }[];
    dailyComments: { date: string; count: number }[];
  };
  platformComparison: {
    platform: string;
    averageRating: number;
    totalViews: number;
  }[];
  predictionMetrics: {
    expectedGrowth: number; // 예상 성장률 (%)
    retentionRate: number; // 유지율 (%)
    churnRate: number; // 이탈률 (%)
  };
}