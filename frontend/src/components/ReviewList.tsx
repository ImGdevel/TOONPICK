import React from 'react';
import { Review } from '../types/review';
import styles from './ReviewList.module.css';

interface ReviewListProps {
  reviews: Review[];
}

const ReviewList: React.FC<ReviewListProps> = ({ reviews }) => {
  return (
    <div className={styles.reviewList}>
      {reviews.map((review) => (
        <div key={review.id} className={styles.reviewCard}>
          <h3>{review.user?.username || '익명'}</h3>
          <p>{review.content}</p>
          <span>Rating: {review.rating}</span>
        </div>
      ))}
    </div>
  );
};

export default ReviewList; 