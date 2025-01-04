import React from 'react';
import styles from './PlatformIcon.module.css';

interface PlatformIconProps {
  platform: string;
  size?: 'small' | 'medium' | 'large';
}

const PlatformIcon: React.FC<PlatformIconProps> = ({ platform, size = 'medium' }) => {
  return (
    <img
      src={`/images/platforms/${platform.toLowerCase()}.png`}
      alt={`${platform} 플랫폼`}
      className={`${styles.platformIcon} ${styles[size]}`}
    />
  );
};

export default PlatformIcon;
