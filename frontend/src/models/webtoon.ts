export type Platform = 'NAVER' | 'KAKAO' | 'RIDI' | 'TOPTOON';
export type StatusType = 'ONGOING' | 'COMPLETED' | 'HIATUS';

export interface Author {
  id: number;
  name: string;
}

export interface Tag {
  id: number;
  name: string;
}

export interface Webtoon {
  id: number;
  thumbnailUrl: string;
  title: string;
  isAdult: boolean;
  status: string;
  publishDay: string;
  platforms: Platform[];
  authors: Author[];
  description: string;
  tags: Tag[];
  totalRatings: number;
  averageRating: number;
  analysisData?: any;
  similarWebtoons?: any[];
}

export interface WebtoonResponse<T = any> {
  success: boolean;
  data?: T;
  total?: number;
  message?: string;
  error?: string;
}
