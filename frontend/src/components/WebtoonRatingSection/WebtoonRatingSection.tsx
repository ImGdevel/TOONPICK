import React from 'react';
import styles from './WebtoonRatingSection.module.css';
import { Review } from '@models/review';
import WebtoonReviewCard from '@/components/WebtoonReviewCard';

interface WebtoonRatingSectionProps {
  averageRating: number;
  reviews: Review[];
  onLike: (reviewId: number) => Promise<void>; // 좋아요 핸들러
  onReport: (reviewId: number) => void; // 리포트 핸들러
}

const WebtoonRatingSection: React.FC<WebtoonRatingSectionProps> = ({ averageRating, reviews, onLike, onReport }) => {
  return (
    <div className={styles.ratingSection}>
      <h2>웹툰 평가</h2>
      <div className={styles.averageRating}>
        <span>평균 별점: {averageRating.toFixed(1)} ⭐</span>
      </div>
      <div className={styles.reviewList}>
        {reviews.map(review => (
          <WebtoonReviewCard 
            key={review.id} 
            review={review} 
            onLike={onLike} 
            onReport={onReport} 
          />
        ))}
      </div>
    </div>
  );
};

export default WebtoonRatingSection;