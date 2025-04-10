import React from 'react';
import { Webtoon } from '@models/webtoon';
import styles from './style.module.css';

interface WebtoonRecommendationSectionProps {
  webtoon: Webtoon;
}

const WebtoonRecommendationSection: React.FC<WebtoonRecommendationSectionProps> = ({ webtoon }) => {
  const { similarWebtoons } = webtoon;

  if (!similarWebtoons || similarWebtoons.length === 0) {
    return null;
  }

  return (
    <section className={styles.recommendationSection}>
      <h2 className={styles.sectionTitle}>비슷한 웹툰</h2>
      <div className={styles.recommendationList}>
        {similarWebtoons.map((webtoon) => (
          <div key={webtoon.id} className={styles.recommendationItem}>
            <img
              src={webtoon.thumbnailUrl}
              alt={webtoon.title}
              className={styles.thumbnail}
            />
            <div className={styles.info}>
              <h3 className={styles.title}>{webtoon.title}</h3>
              <p className={styles.platform}>{webtoon.platform}</p>
              <p className={styles.rating}>평점: {webtoon.averageRating.toFixed(1)}</p>
              <div className={styles.genres}>
                {webtoon.genres.map((genre) => (
                  <span key={genre.id} className={styles.genre}>
                    {genre.name}
                  </span>
                ))}
              </div>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
};

export default WebtoonRecommendationSection; 