import { Platform, SerializationStatus, AgeRating, DayOfWeek } from '@models/enum';
import { Author } from '@models/author';
import { Genre } from '@models/genre';

export interface Webtoon {
  id: number;
  thumbnailUrl: string;
  title: string;
  isAdult: boolean;
  status: string;
  publishDay: string;
  platforms: Platform;
  authors: Author[];
  description: string;
  genre: Genre[];
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
