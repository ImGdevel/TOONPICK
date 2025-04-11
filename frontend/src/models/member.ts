import { Webtoon } from "./webtoon";
import { Review } from "./review";

export interface Member {
  username: string;
  nickname: string;
  role: string;
  profilePicture: string;
  email: string;
  isAdultVerified: boolean;
}

export interface BasicMemberInfo {
  username: string;
  nickname: string;
  profilePicture: string;
  role: string;
}

export interface MemberProfile {
  username: number;
  email: string;
  nickname: string;
  profilePicture: string;
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

}