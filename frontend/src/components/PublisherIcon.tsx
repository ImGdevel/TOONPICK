import React from 'react';
import styles from './PublisherIcon.module.css';

interface PublisherIconProps {
  publisher: string;
  size?: 'small' | 'medium' | 'large';
}

const PublisherIcon: React.FC<PublisherIconProps> = ({ publisher, size = 'medium' }) => {
  const getPublisherLogo = (publisher: string): string => {
    switch (publisher.toLowerCase()) {
      case 'naver':
        return '/images/naver-logo.png';
      case 'kakao':
        return '/images/kakao-logo.png';
      default:
        return '/images/default-publisher.png';
    }
  };

  return (
    <img
      src={getPublisherLogo(publisher)}
      alt={`${publisher} 로고`}
      className={`${styles.publisherIcon} ${styles[size]}`}
    />
  );
};

export default PublisherIcon; 