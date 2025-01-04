import React from 'react';
import { Link } from 'react-router-dom';
import { Webtoon } from '../../types/webtoon';
import styles from './SimilarWebtoons.module.css';

interface SimilarWebtoonsProps {
  currentWebtoonId: number;
  similarWebtoons: Webtoon[];
}

const SimilarWebtoons: React.FC<SimilarWebtoonsProps> = ({
  currentWebtoonId,
  similarWebtoons
}) => {
  return (
    <div className={styles.similarWebtoons}>
      <h3>비슷한 웹툰</h3>
      <div className={styles.grid}>
        {similarWebtoons
          .filter(webtoon => webtoon.id !== currentWebtoonId)
          .slice(0, 4)
          .map(webtoon => (
            <Link 
              key={webtoon.id} 
              to={`/webtoon/${webtoon.id}`}
              className={styles.webtoonCard}
            >
              <img
                src={webtoon.thumbnailUrl}
                alt={webtoon.title}
                className={styles.thumbnail}
              />
              <div className={styles.info}>
                <h4>{webtoon.title}</h4>
                <p>{webtoon.author}</p>
              </div>
            </Link>
          ))}
      </div>
    </div>
  );
};

export default SimilarWebtoons; 