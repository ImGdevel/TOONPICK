import React from 'react';
import styles from './PlatformIcon.module.css';
import { Platform } from '@models/enum';

interface PlatformIconProps {
  platform: Platform;
  size?: 'small' | 'medium' | 'large';
}

const PlatformIcon: React.FC<PlatformIconProps> = ({ platform, size = 'medium' }) => {
  return (
    <img
      src={`/images/platforms/btn_${platform.toLowerCase()}.svg`}
      alt={`${platform} 플랫폼`}
      className={`${styles.platformIcon} ${styles[size]}`}
    />
  );
};

export default PlatformIcon;
