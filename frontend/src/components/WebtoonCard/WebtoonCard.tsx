import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '@models/webtoon';
import styles from './WebtoonCard.module.css';

interface WebtoonCardProps {
  webtoon: Webtoon;
  showPublisher?: boolean;
}

const WebtoonCard: React.FC<WebtoonCardProps> = ({ webtoon, showPublisher = true }) => {
  const authors = webtoon.authors?.map(author => author.name).join(', ') || 'Unknown Author';
  const averageRating = webtoon.averageRating ? webtoon.averageRating.toFixed(1) : '0';
  const truncatedTitle = webtoon.title.length > 30 ? `${webtoon.title.substring(0, 30)}...` : webtoon.title;

  return (
    <div className={styles.webtoonCard}>
      <Link to={`/webtoon/${webtoon.id}`} className={styles.thumbnailContainer}>
        <img src={webtoon.thumbnailUrl} alt={webtoon.title} className={styles.thumbnailImage} />
      </Link>
      <div className={styles.webtoonInfo}>
        <span className={styles.webtoonTitle}>{truncatedTitle}</span>
        <div className={styles.webtoonMeta}>
          <span className={styles.webtoonAuthor}>{authors}</span>
          <span className={styles.webtoonRating}>⭐ {averageRating}</span>
        </div>
      </div>
    </div>
  );
};

export default WebtoonCard; 