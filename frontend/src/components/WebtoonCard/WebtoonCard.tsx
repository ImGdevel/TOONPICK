import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '@models/webtoon';
import styles from './WebtoonCard.module.css';
import PlatformIcon from '../PlatformIcon/PlatformIcon';

interface WebtoonCardProps {
  webtoon: Webtoon;
  showTags?: boolean;
  size?: number;
}

const WebtoonCard: React.FC<WebtoonCardProps> = ({ webtoon, showTags = true, size = 360 }) => {
  const getAuthors = (authors: { name: string }[] | undefined): string => {
    return authors?.map(author => author.name).join(', ') || '작가 없음';
  };

  const formatAverageRating = (rating: number | undefined): string => {
    return rating ? rating.toFixed(1) : '0';
  };

  const truncateTitle = (title: string): string => {
    return title.length > 30 ? `${title.substring(0, 30)}...` : title;
  };

  const authors = getAuthors(webtoon.authors);
  const averageRating = formatAverageRating(webtoon.averageRating);
  const truncatedTitle = truncateTitle(webtoon.title);

  const height = size;
  const width = (size * 220) / 360;

  return (
    <Link to={`/webtoon/${webtoon.id}`} className={styles.webtoonCard} style={{ width: `${width}px`, height: `${height}px` }}>
      
      <div className={styles.thumbnailContainer}>
        <img src={webtoon.thumbnailUrl} alt={webtoon.title} className={styles.thumbnailImage} />
        {showTags && (
          <div className={styles.tagsContainer}>
            <PlatformIcon platform={webtoon.platform} size="small" />
          </div>
        )}
      </div>

      <div className={styles.webtoonInfo}>
        <span className={styles.webtoonTitle}>{truncatedTitle}</span>
        <div className={styles.webtoonMeta}>
          <span className={styles.webtoonAuthor}>{authors}</span>
          <span className={styles.webtoonRating}>⭐ {averageRating}</span>
        </div>
      </div>
    </Link>
  );
};

export default WebtoonCard; 