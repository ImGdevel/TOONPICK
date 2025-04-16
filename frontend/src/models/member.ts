import { Webtoon } from "./webtoon";
import { Review } from "./review";

export interface MemberProfile {
  username: number;
  email: string;
  nickname: string;
  profileImage: string | null;
  level: number;
  ratedWebtoons: number;
  reviewedWebtoons: number;
  collections: number;
  readWebtoons: number;
  points: number;
  bookmarkedWebtoons: number;
  watchedWebtoons: number;
  badges: {
    id: number;
    name: string;
    icon: string;
  }[];
  preferences: {
    genrePreferences: { name: string; value: number }[];
    emotionalTags: string[];
    aiTags: string[];
  };
  favoriteWebtoons: Webtoon[];
  masterpieceWebtoons: Webtoon[];
  readingHistory: {
    webtoon: Webtoon;
    lastReadAt: string;
  }[];
  reviews: Review[];
  topReviews: Review[];
  connectedAccounts: {
    google: boolean;
    naver: boolean;
    kakao: boolean;
  };
  adultSettings: {
    goreFilter: boolean;
    adultContentFilter: boolean;
    violenceFilter: boolean;
  };
}