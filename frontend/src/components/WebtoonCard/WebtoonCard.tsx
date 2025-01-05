import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '@/types/webtoon';
import styles from './WebtoonCard.module.css';

interface WebtoonItemProps {
  webtoon: Webtoon;
  showPublisher?: boolean;
}

const WebtoonCard: React.FC<WebtoonItemProps> = ({ webtoon, showPublisher = true }) => {
  const authors = webtoon.authors?.map(author => author.name).join(', ') || 'Unknown Author';
  const averageRating = webtoon.averageRating ?? 'N/A';
  const truncatedTitle = webtoon.title.length > 30 ? `${webtoon.title.substring(0, 30)}...` : webtoon.title;

  return (
    <div className={styles['webtoon-item']}>
      <Link to={`/webtoon/${webtoon.id}`} className={styles['thumbnail-container']}>
        <img src={webtoon.thumbnailUrl} alt={webtoon.title} className={styles['thumbnail-image']} />
      </Link>
      <div className={styles['webtoon-info']}>
        <span className={styles['webtoon-title']}>{truncatedTitle}</span>
        <div className={styles['webtoon-meta']}>
          <span className={styles['webtoon-author']}>{authors}</span>
          <span className={styles['webtoon-rating']}>⭐ {averageRating}</span>
        </div>
      </div>
    </div>
  );
};

export default WebtoonCard; 