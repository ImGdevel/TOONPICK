export enum Platform {
  NAVER = 'NAVER',
  KAKAO = 'KAKAO',
  KAKAOPAGE = 'KAKAOPAGE',
  LEZHIN = 'LEZHIN',
  BOMTOON = 'BOMTOON',
  OTHER = 'OTHER'
}

export enum SerializationStatus {
  ONGOING = 'ONGOING',
  COMPLETED = 'COMPLETED',
  HIATUS = 'HIATUS'
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
  analysisData: AnalysisData;
  similarWebtoons: SimilarWebtoon[];
}

export interface Author {
  id: number;
  name: string;
}

export interface Genre {
  id: number;
  name: string;
}

export interface AnalysisData {
  genreDistribution: { genre: string; percentage: number }[];
  audienceAge: { age: string; percentage: number }[];
  genderDistribution: { gender: string; percentage: number }[];
  plotComplexity: number;
  characterDevelopment: number;
  worldBuilding: number;
  pacing: number;
  dialogueQuality: number;
  artStyle: number;
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

export interface MemberProfile {
  id: number;
  username: string;
  email: string;
  nickname: string;
  profileImage: string;
}
