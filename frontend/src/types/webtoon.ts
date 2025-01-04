export type Platform = 'NAVER' | 'KAKAO' | 'RIDI' | 'TOPTOON';
export type StatusType = 'ONGOING' | 'COMPLETED' | 'HIATUS';

export interface Webtoon {
  id: number;
  title: string;
  thumbnailUrl: string;
  platforms: Platform[];
  authors: Array<{ id: string; name: string }>;
  genres: Array<{ id: string; name: string }>;
  description: string;
  status: StatusType;
  publishDay: string;
  isAdult: boolean;
  publisher?: string;
  rating?: number;
}