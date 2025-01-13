import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '@models/webtoon';
import styles from './WebtoonCard.module.css';
import PlatformIcon from '../PlatformIcon/PlatformIcon';

interface WebtoonCardProps {
  webtoon: Webtoon;
  showTags?: boolean;
}

const WebtoonCard: React.FC<WebtoonCardProps> = ({ webtoon, showTags = true }) => {
  const authors = webtoon.authors?.map(author => author.name).join(', ') || '작가 없음';
  const averageRating = webtoon.averageRating ? webtoon.averageRating.toFixed(1) : '0';
  const truncatedTitle = webtoon.title.length > 30 ? `${webtoon.title.substring(0, 30)}...` : webtoon.title;

  return (
    <Link to={`/webtoon/${webtoon.id}`} className={styles.webtoonCard}>
      
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