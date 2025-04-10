import React from 'react';
import styles from './style.module.css';
import { Platform } from '@models/webtoon';

interface PlatformIconProps {
  platform: Platform;
  size?: 'small' | 'medium' | 'large' | number;
}

const PlatformIcon: React.FC<PlatformIconProps> = ({ platform, size = 'medium' }) => {
  const getSizeStyle = () => {
    if (typeof size === 'number') {
      return {
        width: `${size}px`,
        height: `${size}px`
      };
    }
    return styles[size];
  };

  const getPlatformIcon = () => {
    switch (platform) {
      case Platform.NAVER:
        return '/images/platforms/btn_naver.svg';
      case Platform.KAKAO:
        return '/images/platforms/btn_kakao.svg';
      case Platform.KAKAOPAGE:
        return '/images/platforms/btn_kakaopage.svg';
      case Platform.BOMTOON:
        return '/images/platforms/btn_bomtoon.svg';
      case Platform.LEZHIN:
        return '/images/platforms/btn_lezhin.svg';
      default:
        return '/images/platforms/btn_other.svg';
    }
  };

  return (
    <img
      src={getPlatformIcon()}
      alt={`${platform} 플랫폼`}
      className={typeof size === 'number' ? styles.platformIcon : `${styles.platformIcon} ${getSizeStyle()}`}
      style={typeof size === 'number' ? getSizeStyle() : undefined}
    />
  );
};

export default PlatformIcon;
