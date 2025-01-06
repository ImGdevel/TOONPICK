import { Webtoon, Platform } from './webtoon';

export interface StatusBadgeProps {
  text: string;
}

export interface PlatformIconProps {
  platform: Platform;
}

export interface BookmarkButtonProps {
  isBookmarked: boolean;
  onClick: () => void;
}

export interface FavoriteButtonProps {
  isFavorite: boolean;
  onClick: () => void;
}

export interface WebtoonTagProps {
  text: string;
} 