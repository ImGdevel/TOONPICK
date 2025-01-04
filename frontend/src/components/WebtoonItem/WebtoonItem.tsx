import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '../../types/webtoon';
import StarRating from '@components/common/StarRating';
import StatusBadge from '../StatusBadge/index';
import PublisherIcon from '../PublisherIcon';
import styles from './WebtoonItem.module.css';

interface WebtoonItemProps {
  webtoon: Webtoon;
  showPublisher?: boolean;
}

const WebtoonItem: React.FC<WebtoonItemProps> = ({ 
  webtoon,
  showPublisher = true
}) => {
  return (
    <Link to={`/webtoon/${webtoon.id}`} className={styles.webtoonItem}>
      <div className={styles.thumbnail}>
        <img src={webtoon.thumbnailUrl} alt={webtoon.title} />
        {showPublisher && webtoon.publisher && (
          <div className={styles.publisher}>
            <PublisherIcon publisher={webtoon.publisher} size="small" />
          </div>
        )}
      </div>
      <div className={styles.info}>
        <h3 className={styles.title}>{webtoon.title}</h3>
        <p className={styles.author}>{webtoon.author}</p>
        <div className={styles.meta}>
          <StarRating rating={webtoon.rating || 0} size="small" />
          <StatusBadge status={webtoon.status} size="small" />
        </div>
      </div>
    </Link>
  );
};

export default WebtoonItem; 