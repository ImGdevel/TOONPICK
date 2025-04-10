import React from 'react';
import { Webtoon } from '@models/webtoon';
import styles from './style.module.css';

interface WebtoonRatingSectionProps {
  webtoon: Webtoon;
}

const WebtoonRatingSection: React.FC<WebtoonRatingSectionProps> = ({ webtoon }) => {
  const { totalRatings, averageRating } = webtoon;

  return (
    <section className={styles.ratingSection}>
      <h2 className={styles.sectionTitle}>평점</h2>
      <div className={styles.ratingSummary}>
        <div className={styles.averageRating}>
          <span className={styles.ratingValue}>{averageRating.toFixed(1)}</span>
          <span className={styles.ratingMax}>/ 10</span>
        </div>
        <div className={styles.totalRatings}>
          <span className={styles.ratingCount}>{totalRatings.toLocaleString()}</span>
          <span className={styles.ratingLabel}>명이 평가함</span>
        </div>
      </div>
    </section>
  );
};

export default WebtoonRatingSection;